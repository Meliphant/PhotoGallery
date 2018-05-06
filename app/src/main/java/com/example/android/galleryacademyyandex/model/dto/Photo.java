package com.example.android.galleryacademyyandex.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * DTO for deserialization from JSON
 *
 * Created by Meliphant on 21.04.2018.
 */

public class Photo implements Parcelable {
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("src")
    @Expose
    private final Src src;

    private Photo(Parcel in) {
        id = in.readInt();
        src = in.readParcelable(getClass().getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Src getSrc() {
        return src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(src, flags);
    }
}
