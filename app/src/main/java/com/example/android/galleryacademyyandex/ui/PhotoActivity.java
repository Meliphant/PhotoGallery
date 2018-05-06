package com.example.android.galleryacademyyandex.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.galleryacademyyandex.R;
import com.example.android.galleryacademyyandex.app.App;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Meliphant on 14.04.2018.
 */

public class PhotoActivity extends AppCompatActivity {

    public static final String INTENT_PHOTO = "photo";
    private static final String SHARE_TYPE = "text/html";
    private static final String DATE_FORMAT = "ddMMyyyy_HHmmss";

    private Context mContext;
    private boolean isInternetAvailable = true;
    private String mImageUrl;
    private ProgressBar mLoadingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mLoadingItem = findViewById(R.id.pb_loading_indicator_photo);
        mLoadingItem.setVisibility(View.VISIBLE);

        PhotoView mSelectedImage = findViewById(R.id.iv_photo_activity);
        final Intent startIntent = getIntent();
        mImageUrl = startIntent.getStringExtra(INTENT_PHOTO);

        if (App.isInternetAvailable(this)) {
            setImagePicasso(mImageUrl, mSelectedImage, this);
        } else {
            Toast.makeText(this, R.string.notification_no_internet_connection, Toast.LENGTH_LONG).show();
            isInternetAvailable = false;
            mLoadingItem.setVisibility(View.GONE);
        }
    }

    private void setImagePicasso(String url, ImageView imageView, Context context) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mLoadingItem.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mLoadingItem.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.action_share:
                shareItemMenuIntent();
                return true;
            case R.id.action_download:
                if (isStoragePermissionGranted()) {
                    imageDownload(mImageUrl, mContext);
                    Toast.makeText(this, R.string.photo_activity_downloaded_alert, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_about:
                aboutItemMenuIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isInternetAvailable) {
            visibilityMenuItemsHandler(menu, true, 255);
        } else {
            visibilityMenuItemsHandler(menu, false, 130);
        }
        return true;
    }

    private void visibilityMenuItemsHandler(Menu menu, boolean isEnabled, int alpha) {
        MenuItem itemDownload = menu.findItem(R.id.action_download).setEnabled(isEnabled);
        MenuItem itemShare = menu.findItem(R.id.action_share).setEnabled(isEnabled);
        itemDownload.getIcon().setAlpha(alpha);
        itemShare.getIcon().setAlpha(alpha);
    }

    private void shareItemMenuIntent() {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType(SHARE_TYPE);
        intentShare.putExtra(android.content.Intent.EXTRA_TEXT,
                getString(R.string.action_share_extra_text) + mImageUrl);
        Intent chooser = Intent.createChooser(intentShare, getString(R.string.action_share_message));
        if (intentShare.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void aboutItemMenuIntent() {
        Intent intentAbout = new Intent(this, AboutActivity.class);
        startActivity(intentAbout);
    }

    // Save image.
    private void imageDownload(String mImageUrl, Context context) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String timestamp = simpleDateFormat.format(date);
        Picasso.with(context)
                .load(mImageUrl)
                .into(getTarget("IMG_" + timestamp + ".jpg"));
    }

    private Target getTarget(final String name) {
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                                + "/" + "Camera" + "/" + name);
                        try {
                            Log.d("Download", file.toString());
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("StoragePermission", "Permission is granted");
                return true;
            } else {
                Log.d("StoragePermission", "Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.d("StoragePermission", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("RequestPermission", "Permission: " + permissions[0] + " was " + grantResults[0]);
        }
    }
}
