package com.example.randomfight.api

import com.example.randomfight.entity_model.Enemy
import com.example.randomfight.entity_model.enemyAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class randomEnemyApi {

        companion object{
            const val BASE_URL = "https://digimon-api.vercel.app/api/"
        }

    fun getAllDigimons(): Call<List<Enemy>>{
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val countriesAPI = retrofit.create(enemyAPI::class.java)

        return countriesAPI.getAllEnemy()
    }

}

