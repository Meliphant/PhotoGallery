package com.example.android.galleryacademyyandex.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * DTO for deserialization from JSON
 *
 * Created by Meliphant on 21.04.2018.
 */

public class DtoPhotoModel {

    @SerializedName("photos")
    @Expose
    private final List<Photo> photos = null;

    public List<Photo> getPhotos() {
        return photos;
    }
}
