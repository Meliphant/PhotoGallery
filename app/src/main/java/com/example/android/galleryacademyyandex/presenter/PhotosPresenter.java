package com.example.android.galleryacademyyandex.presenter;

import android.util.Log;

import com.example.android.galleryacademyyandex.model.PhotosData;
import com.example.android.galleryacademyyandex.model.PhotosDataCallback;
import com.example.android.galleryacademyyandex.model.dto.Photo;
import com.example.android.galleryacademyyandex.ui.PhotosView;

import java.util.List;

public class PhotosPresenter implements PhotosDataCallback {

    private static final String TAG = PhotosPresenter.class.getSimpleName();
    private PhotosView photosView;
    private final PhotosData photosData;

    public PhotosPresenter(PhotosData photosData) {
        Log.d(TAG, "PhotosPresenter constructor");
        this.photosData = photosData;
    }

    @Override
    public void onSuccessLoad(List<Photo> books) {
        Log.d(TAG, "onSuccessLoad");
        photosView.displayPhotos(books);
    }

    @Override
    public void onErrorLoad() {
        Log.d(TAG, "onErrorLoad");
        photosView.showErrorMsg();
    }

    public void findPhotos(PhotosView photosView, String query) {
        Log.d(TAG, "findPhotos");
        this.photosView = photosView;
        if (!query.isEmpty()) {
            photosData.getPhotosQuery(query, this);
            return;
        }
        photosView.displayPhotos(photosData.getListResult());
    }
}
