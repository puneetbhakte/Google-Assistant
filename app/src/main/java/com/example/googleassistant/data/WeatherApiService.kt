package com.example.googleassistant.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current?")
    fun getWeatherData(@Query("lat") lat : String,@Query("lon") lon : String,@Query("key") key : String,@Query("include") include:String):Call<Weather>
}