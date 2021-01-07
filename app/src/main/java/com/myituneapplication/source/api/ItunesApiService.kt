package com.myituneapplication.source.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * A set of API for iTune.
 */
interface ItunesApiService {

//    @POST("search?media=music&country=au")
//    @POST("search")
    @POST("search?media=music")
    fun searchSong(
        @Query("term") keyword: String,
        @Query("limit") limit: Int
    ): Call<ItuneResponse>

//    @POST("search?media=music&country=au")
//    @POST("search")
    @POST("search?media=music")
    fun searchSong(
        @Query("term") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ItuneResponse>
}