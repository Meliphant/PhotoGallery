package com.example.android.galleryacademyyandex.model.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * DTO for deserialization from JSON
 *
 * Created by Meliphant on 21.04.2018.
 */

public class Src implements Parcelable{
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private final String medium;

    private Src(Parcel in) {
        large = in.readString();
        medium = in.readString();
    }

    public static final Creator<Src> CREATOR = new Creator<Src>() {
        @Override
        public Src createFromParcel(Parcel in) {
            return new Src(in);
        }

        @Override
        public Src[] newArray(int size) {
            return new Src[size];
        }
    };

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(large);
        dest.writeString(medium);
    }
}
