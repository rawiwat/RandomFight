package com.example.randomfight.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.graphics.Canvas
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.randomfight.R
import com.example.randomfight.RNG
import com.example.randomfight.api.randomEnemyApi
import com.example.randomfight.entity_model.Enemy
import com.example.randomfight.entity_model.Player
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class FightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        getAllDigimons()

        findViewById<Button>(R.id.backButton).setOnClickListener{
            goBackConfirmation()
        }
        //gotta get value of player first but I'll do that later when database is implemented
        val playerStats = Player()
        val playerHealthView = findViewById<TextView>(R.id.playerHp)
        val playerMaxHealthView = findViewById<TextView>(R.id.playerMaxHp)
        val playerAttackView = findViewById<TextView>(R.id.playerAP)
        val playerDefenseView = findViewById<TextView>(R.id.playerDefense)
        val playerSpeedView = findViewById<TextView>(R.id.playerSpeed)
        val playerHealingView = findViewById<TextView>(R.id.playerHealing)

        playerHealthView.text = playerStats.health.toString()
        playerMaxHealthView.text = "/" + "${playerStats.health}"
        playerAttackView.text = playerStats.attack.toString()
        playerDefenseView.text = playerStats.defense.toString()
        playerSpeedView.text = playerStats.speed.toString()
        playerHealingView.text = playerStats.healing.toString()

        val firstEnemyStats = RNG().getRandomEnemyStats(1,1)
        val firstEnemyHealthView = findViewById<TextView>(R.id.enemyHp)
        val firstEnemyMaxHealthView = findViewById<TextView>(R.id.enemyMaxHp)
        val firstEnemyAttackView = findViewById<TextView>(R.id.enemyAP)
        val firstEnemyDefenseView = findViewById<TextView>(R.id.enemyDefense)
        val firstEnemySpeedView = findViewById<TextView>(R.id.enemySpeed)
        val firstEnemyHealingView = findViewById<TextView>(R.id.enemyHealing)

        firstEnemyHealthView.text = firstEnemyStats.health.toString()
        firstEnemyMaxHealthView.text = "/" + "${firstEnemyStats.health}"
        firstEnemyAttackView.text = firstEnemyStats.attack.toString()
        firstEnemyDefenseView.text = firstEnemyStats.defense.toString()
        firstEnemySpeedView.text = firstEnemyStats.speed.toString()
        firstEnemyHealingView.text = firstEnemyStats.healing.toString()
        // will upgrade with databind later

    }

    private fun getAllDigimons(){
        randomEnemyApi().getAllDigimons().enqueue(object:Callback<List<Enemy>>{
            override fun onResponse(call: Call<List<Enemy>>, response: Response<List<Enemy>>) {
                if (response.isSuccessful){
                    val listOfDigimons = response.body()?.map { it } ?: emptyList()
                    println(listOfDigimons)
                    val result = RNG().getRandomDigimon(listOfDigimons)
                    val executioner = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())
                    result.let {
                        executioner.execute {
                            val imageUrl = it.img
                            try {
                                val enemyImage = findViewById<ImageView>(R.id.enemyImage)
                                val decoder = java.net.URL(imageUrl).openStream()
                                val enemyImageBitmap: Bitmap = BitmapFactory.decodeStream(decoder)
                                val currentBitmap = Bitmap.createBitmap(
                                    enemyImageBitmap.width,
                                    enemyImageBitmap.height,
                                    Bitmap.Config.ARGB_8888
                                )
                                val canvas = android.graphics.Canvas(currentBitmap)
                                canvas.drawBitmap(enemyImageBitmap, 0f, 0f, null)
                                handler.post {
                                    enemyImage.setImageBitmap(enemyImageBitmap)
                                }
                            } catch (e:java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Enemy>>, t: Throwable) {
                Toast.makeText(this@FightActivity,"get random enemy failed 1",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
    goBackConfirmation()
    }

    fun goBackConfirmation(){
        val backPressedMenu = findViewById<ImageView>(R.id.backPressMenu)
        val backPressedMenuText = findViewById<TextView>(R.id.backPressMenuText)
        val backPressedYes = findViewById<Button>(R.id.yesBackPressedMenu)
        val backPressedNo = findViewById<Button>(R.id.NoBackPressedMenu)
        backPressedMenu.visibility = VISIBLE
        backPressedMenuText.visibility = VISIBLE
        backPressedYes.visibility = VISIBLE
        backPressedNo.visibility = VISIBLE
        backPressedYes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        backPressedNo.setOnClickListener {
            backPressedMenu.visibility = GONE
            backPressedMenuText.visibility = GONE
            backPressedYes.visibility = GONE
            backPressedNo.visibility = GONE
        }
    }

}