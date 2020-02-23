package com.example.minibus.data.network;

import com.example.minibus.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class AppApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = AppConstants.BASE_URL;
    public static final int BASE_TIMEOUT_SEC = AppConstants.DEFAULT_TIMEOUT_SEC;

    public static AppApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit.create(AppApiService.class);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor((message) -> {
            Timber.tag("OkHttp").d(message);
        });

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpLoggingInterceptor.redactHeader("Authorization");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(httpLoggingInterceptor);

        return httpClient.connectTimeout(BASE_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(BASE_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(BASE_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
    }
}
