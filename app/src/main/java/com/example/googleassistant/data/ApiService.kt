package com.example.googleassistant.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/top-headlines")
    fun getnews(@Query("country") country : String, @Query("category") category : String?,@Query("language") language : String, @Query("apiKey") key : String):Call<NewsArticles>


}