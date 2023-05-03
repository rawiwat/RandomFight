package com.example.randomfight.entity_model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.Serializable

data class Enemy (
    var name:String,
    var img:String,
    var level:String
)

interface enemyAPI{
    @GET("digimon")
    fun getAllEnemy(): Call<List<Enemy>>
}