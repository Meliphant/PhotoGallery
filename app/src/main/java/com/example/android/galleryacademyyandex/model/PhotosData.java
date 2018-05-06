package com.example.android.galleryacademyyandex.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.galleryacademyyandex.app.App;
import com.example.android.galleryacademyyandex.app.AppQueryPreferences;
import com.example.android.galleryacademyyandex.model.dto.DtoPhotoModel;
import com.example.android.galleryacademyyandex.model.dto.Photo;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosData {

    private static final String TAG = PhotosData.class.getSimpleName();
    private final PhotosApi photosApi;
    private List<Photo> listResult;

    public PhotosData() {
        this.photosApi = App.getApi();
    }

    public void getPhotosQuery(String searchQuery, final PhotosDataCallback photosDataCallback) {
        Log.d(TAG, "getPhotosQuery");

        photosApi.getData(searchQuery, AppQueryPreferences.QUERY_TO_API_RESULTS_NUMBER, AppQueryPreferences.QUERY_TO_API_PAGES_NUMBER)
                .enqueue(new Callback<DtoPhotoModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DtoPhotoModel> call, @NonNull Response<DtoPhotoModel> response) {
                        if (response.body() == null) {
                            photosDataCallback.onErrorLoad();
                            return;
                        }

                        listResult = Objects.requireNonNull(response.body()).getPhotos();
                        photosDataCallback.onSuccessLoad(listResult);

                    }

                    @Override
                    public void onFailure(@NonNull Call<DtoPhotoModel> call, @NonNull Throwable t) {
                        photosDataCallback.onErrorLoad();
                    }
                });
    }

    public List<Photo> getListResult() {
        return listResult;
    }
}
