package com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices;

import com.example.apc4.retrofitwithrepositrystructure.BuildConfig;
import com.example.apc4.retrofitwithrepositrystructure.Constants.ApiTags;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class RestClient {

    private static IRestClient retrofitCalls;
    private static IRestClient retrofitGoogleApiCalls;

    public static IRestClient getClient() {
        if (retrofitCalls == null) {
            OkHttpClient okHttpClient;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
                    HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
            okHttpClient.interceptors().add(loggingInterceptor);
            Retrofit.Builder builder = new Retrofit.Builder();
            Retrofit client = builder.baseUrl(ApiTags.BASE_SERVER_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitCalls = client.create(IRestClient.class);
        }
        return retrofitCalls;
    }

    public static IRestClient getGooglePlaceApiClient() {
        if (retrofitGoogleApiCalls == null) {
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(ApiTags.GOOGLE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitGoogleApiCalls = client.create(IRestClient.class);
        }
        return retrofitGoogleApiCalls;
    }
}
