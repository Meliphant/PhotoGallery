package com.example.android.galleryacademyyandex.model;

import com.example.android.galleryacademyyandex.model.dto.Photo;

import java.util.List;

public interface PhotosDataCallback {

    void onSuccessLoad(List<Photo> photos);

    void onErrorLoad();

}
