package com.example.android.galleryacademyyandex.ui;

import com.example.android.galleryacademyyandex.model.dto.Photo;

import java.util.List;

public interface PhotosView {
    void displayPhotos(List<Photo> photos);
    void showErrorMsg();
}
