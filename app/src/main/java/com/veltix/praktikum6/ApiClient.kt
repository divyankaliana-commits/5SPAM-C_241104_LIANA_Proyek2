package com.veltix.praktikum6

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.risethub.web.id/v1/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor { message -> Log.i("OkHttp", message) }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val filmApi: FilmApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmApi::class.java)
    }
}