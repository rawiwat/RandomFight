package com.example.randomfight.api

import com.example.randomfight.entity_model.Enemy
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface randomEnemyApi {

    @GET("digimon")
    fun getAllEnemy():Call<List<Enemy>>

    @GET("digimon/name/{name}")
    fun getEnemyByName(@Path("name")name:String?) : Call<Enemy>

    class Factory {
        companion object{
            const val BASE_URL = "https://digimon-api.vercel.app/api/"
            val gson = Gson()
            val logger = HttpLoggingInterceptor().apply{
                level = HttpLoggingInterceptor.Level.BODY
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .retryOnConnectionFailure(false)
                .readTimeout(35, TimeUnit.SECONDS)
                .build()

            fun create(): randomEnemyApi{

                return Retrofit.Builder()
                    .baseUrl(randomEnemyApi.Factory.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(randomEnemyApi::class.java)
            }
        }
    }
}