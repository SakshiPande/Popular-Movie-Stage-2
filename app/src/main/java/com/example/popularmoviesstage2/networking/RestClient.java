package com.example.popularmoviesstage2.networking;

import com.example.popularmoviesstage2.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {


    public static Retrofit retrofitService() {

        return new Retrofit.Builder()
                .baseUrl(Constant.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
