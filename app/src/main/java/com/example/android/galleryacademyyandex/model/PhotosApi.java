package com.example.android.galleryacademyyandex.model;

import com.example.android.galleryacademyyandex.model.dto.DtoPhotoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Meliphant on 21.04.2018.
 */

public interface PhotosApi {
    @GET("/v1/search")
    Call<DtoPhotoModel> getData(@Query("query") String query, @Query("per_page") int resultsNumber,
                                @Query("page") int pagesNumber);
}
