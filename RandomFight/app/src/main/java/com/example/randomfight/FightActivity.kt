package com.example.randomfight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.randomfight.api.randomEnemyApi
import com.example.randomfight.entity_model.Enemy
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FightActivity : AppCompatActivity(), Callback<List<Enemy>> {

    lateinit var retrofitInterface:randomEnemyApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val gson = Gson()

        retrofitInterface = Retrofit.Builder()
            .baseUrl(randomEnemyApi.Factory.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(randomEnemyApi::class.java)

        getAllDigimon()
    }

    override fun onResponse(call: Call<List<Enemy>>, response:Response<List<Enemy>>) {
            if (response.isSuccessful){
                val listOfDigimons = response.body()?.map { it } ?: emptyList()
                println(listOfDigimons)
                val listOfDigimonNames = mutableListOf<String?>()
                for(digimon in listOfDigimons){
                    listOfDigimonNames.add(digimon.name)
                }
                val result = RNG().getRandomDigimon(listOfDigimonNames)
                getRandomDigimon(result)
            }

    }

    override fun onFailure(call: Call<List<Enemy>>, t: Throwable) {
        Toast.makeText(this,"get image failed",Toast.LENGTH_LONG).show()
    }

    private fun getAllDigimon(){
        retrofitInterface.getAllEnemy().enqueue(this)
    }

    private fun getRandomDigimon(name:String?){
        var result:String? = null
        retrofitInterface.getEnemyByName(name).enqueue(object : Callback<Enemy>{
            override fun onResponse(call: Call<Enemy>, response: Response<Enemy>) {
                if(response.isSuccessful){
                    response.body().let {
                        result = it?.img
                        val enemyImage = findViewById<ImageView>(R.id.enemyImage)
                        Glide.with(this@FightActivity).load(result).into(enemyImage)
                    }
                }
            }

            override fun onFailure(call: Call<Enemy>, t: Throwable) {

            }
        })
    }
}