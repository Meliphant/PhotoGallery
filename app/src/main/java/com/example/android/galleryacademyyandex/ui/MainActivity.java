package com.example.android.galleryacademyyandex.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.galleryacademyyandex.IdlingResource.SimpleIdlingResource;
import com.example.android.galleryacademyyandex.R;
import com.example.android.galleryacademyyandex.app.App;
import com.example.android.galleryacademyyandex.app.AppQueryPreferences;
import com.example.android.galleryacademyyandex.model.dto.Photo;
import com.example.android.galleryacademyyandex.presenter.PhotosPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotosView {

    // Constant for number of columns for a grid layout.
    private static final int COLUMN_NUMBER_PORTRAIT = 2;
    private static final int COLUMN_NUMBER_LANDSCAPE = 3;
    // Constant for debug notification.
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private String mSearchQuery;
    private TextView mEmptyView;
    private ProgressBar mLoadingItem;
    private RecyclerView mRecyclerView;
    private List<Photo> mListOfObjects;
    private RecyclerView.Adapter mAdapter;
    private PhotosPresenter mPhotosPresenter;
    private boolean isInternetAvailable = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private StaggeredGridLayoutManager mLayoutManager;


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        getIdlingResource();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryMainActivity));
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSearchQuery == null || mSearchQuery.isEmpty()) {
                    prepareToPresentPhotos(AppQueryPreferences.QUERY_TO_API_PICTURES_THEME);
                } else {
                    prepareToPresentPhotos(mSearchQuery);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        prepareToPresentPhotos(AppQueryPreferences.QUERY_TO_API_PICTURES_THEME);
    }

    private void setupViews() {
        mEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLoadingItem = findViewById(R.id.pb_loading_indicator_main);
        mPhotosPresenter = App.getPhotosPresenter();
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryMainActivity);
        toolbar = findViewById(R.id.toolbar_main);
    }

    // Set staggered grid to the RecyclerView.
    private void setUpRecycler(List<Photo> listOfObjects) {
        mAdapter = new PhotosAdapter(this, listOfObjects);
        if (getScreenOrientation().equals("portrait")) {
            mLayoutManager = new StaggeredGridLayoutManager(COLUMN_NUMBER_PORTRAIT, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(COLUMN_NUMBER_LANDSCAPE, StaggeredGridLayoutManager.VERTICAL);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    private void showEmptyResult(int emptyViewStatus, int recyclerViewStatus, int message) {
        mEmptyView.setVisibility(emptyViewStatus);
        mEmptyView.setText(message);
        mRecyclerView.setVisibility(recyclerViewStatus);
    }

    @Override
    public void showErrorMsg() {
        Log.d(TAG, "showErrorMsg");
        showEmptyResult(View.VISIBLE, View.GONE, R.string.activity_main_search_empty);
        mLoadingItem.setVisibility(View.GONE);
        mListOfObjects.clear();
        mRecyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        Log.d(TAG, "displayPhotos");
        if (photos == null || photos.isEmpty()) {
            showErrorMsg();
            return;
        }

        mListOfObjects.clear();
        mRecyclerView.getRecycledViewPool().clear();
        mLoadingItem.setVisibility(View.GONE);
        mListOfObjects.addAll(photos);
        mAdapter.notifyDataSetChanged();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        if (mListOfObjects.size() == 0) {
            showEmptyResult(View.VISIBLE, View.GONE, R.string.activity_main_search_empty);
        } else {
            Log.d(TAG, "prepareToPresentPhotos mListOfObjects.size() != 0");
            showEmptyResult(View.GONE, View.VISIBLE, R.string.activity_main_search_empty);
        }
    }

    private void prepareToPresentPhotos(String searchQuery) {
        Log.d(TAG, "prepareToPresentPhotos");
        mLoadingItem.setVisibility(View.VISIBLE);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        if (App.isInternetAvailable(this)) {
            isInternetAvailable = true;
            Log.d(TAG, "isInternetAvailable");
            mListOfObjects = new ArrayList<>();
            setUpRecycler(mListOfObjects);
            mPhotosPresenter.findPhotos(this, searchQuery);
        } else {
            isInternetAvailable = false;
            Log.d(TAG, "isInternetAvailable = false " + isInternetAvailable);
            Toast.makeText(this, R.string.notification_no_internet_connection, Toast.LENGTH_LONG).show();
            showEmptyResult(View.VISIBLE, View.GONE, R.string.notification_no_internet_connection);
            mLoadingItem.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, item);

        final MenuItem searchItem = item.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint(getResources().getString(R.string.action_search_hint));
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
            searchItem.expandActionView();
            searchView.setQuery(mSearchQuery, true);
            searchView.clearFocus();
        }

        // Handle expand and close actions on searchItem.
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            // After click on search icon.
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                showKeyboard();
                return true;
            }

            // After click on back arrow icon.
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                prepareToPresentPhotos(AppQueryPreferences.QUERY_TO_API_PICTURES_THEME);
                if (mListOfObjects.size() == 0)
                    showEmptyResult(View.GONE, View.VISIBLE, R.string.activity_main_search_empty);

                MainActivity.this.setTitle(getResources().getString(R.string.app_name));
                hideKeyboard(searchView);
                searchView.setQuery("", false);
                mSearchQuery = "";
                return true;
            }
        });
        // Handle search action.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {

                prepareToPresentPhotos(searchQuery);
                if (mListOfObjects != null && mListOfObjects.size() == 0 && isInternetAvailable) {
                    showEmptyResult(View.GONE, View.VISIBLE, R.string.activity_main_search_empty);
                } else if (isInternetAvailable) {
                    showEmptyResult(View.VISIBLE, View.GONE, R.string.activity_main_search_empty);
                } else {
                    showEmptyResult(View.VISIBLE, View.GONE, R.string.notification_no_internet_connection);
                }

                searchView.clearFocus();
                mSearchQuery = searchQuery;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void hideKeyboard(SearchView searchView) {
        if (searchView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
        }
    }

    private String getScreenOrientation() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return "portrait";
        } else {
            return "landscape";
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager.setSpanCount(COLUMN_NUMBER_LANDSCAPE);
        } else {
            mLayoutManager.setSpanCount(COLUMN_NUMBER_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.setAdapter(null);
    }
}