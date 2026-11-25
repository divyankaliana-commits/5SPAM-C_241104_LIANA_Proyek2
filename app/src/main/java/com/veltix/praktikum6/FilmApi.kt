package com.veltix.praktikum6

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApi {
    @GET("films")
    fun getFilms(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ApiResponse<FilmListData>>

    @GET("films/{id}")
    fun getFilm(@Path("id") id: Int): Call<ApiResponse<Film>>
}