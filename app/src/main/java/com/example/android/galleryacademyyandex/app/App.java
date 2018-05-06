package com.example.android.galleryacademyyandex.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.galleryacademyyandex.R;
import com.example.android.galleryacademyyandex.model.PhotosApi;
import com.example.android.galleryacademyyandex.model.PhotosData;
import com.example.android.galleryacademyyandex.presenter.PhotosPresenter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meliphant on 21.04.2018.
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static PhotosApi photosApi;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;
    private static PhotosPresenter photosPresenter;

    public static PhotosApi getApi() {
        return photosApi;
    }

    private static Retrofit getClient(String baseUrl, OkHttpClient okHttpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static PhotosPresenter getPhotosPresenter() {
        Log.d(TAG, "getPhotosPresenter");
        return photosPresenter;
    }

    /**
     * Check if internet is available on a device
     *
     * @param context of application
     * @return internet status
     */
    public static boolean isInternetAvailable(Context context) {
        Log.d(TAG, "isInternetAvailable");
        ConnectivityManager mConMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return mConMgr != null
                && mConMgr.getActiveNetworkInfo() != null
                && mConMgr.getActiveNetworkInfo().isAvailable()
                && mConMgr.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        okHttpClient = okHttpClient();
        retrofit = getClient(AppQueryPreferences.QUERY_TO_API_URL, okHttpClient);
        photosApi = retrofit.create(PhotosApi.class);
        photosPresenter = new PhotosPresenter(new PhotosData());
    }

    private OkHttpClient okHttpClient() {
        okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        getString(R.string.pexels_key));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
        return okHttpClient;
    }

}
