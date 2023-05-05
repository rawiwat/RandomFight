package com.example.randomfight.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.randomfight.R
import com.example.randomfight.entity_model.Player

class MainActivity : ComponentActivity() {

    val playerStats = Player()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fightButton = findViewById<Button>(R.id.fightButton)
        val upgradeButton = findViewById<Button>(R.id.upgradeButton)


        fightButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intent = Intent(this, FightActivity::class.java)
                val playerCSVString = playerStats.toCSVString()
                intent.putExtra("Player", playerCSVString)
                setResult(Activity.RESULT_OK,intent)
                startActivityForResult(intent,1)
            } else {
                Toast.makeText(this, "check yer internet", Toast.LENGTH_LONG).show()
            }
        }

        upgradeButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intent = Intent(this, UpgradeActivity::class.java)
                val playerCSVString = playerStats.toCSVString()
                intent.putExtra("Player", playerCSVString)
                setResult(Activity.RESULT_OK,intent)
                startActivityForResult(intent,1)
            } else {
                Toast.makeText(this, "check yer internet", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val player = data?.getStringExtra("Player")
            player.let {
                val playerCSVStringSplited = it?.split(",")
                playerStats.level = playerCSVStringSplited?.get(0)?.toInt() ?: Player().level
                playerStats.attack = playerCSVStringSplited?.get(1)?.toInt() ?: Player().attack
                playerStats.health = playerCSVStringSplited?.get(2)?.toInt() ?: Player().health
                playerStats.speed = playerCSVStringSplited?.get(3)?.toInt() ?: Player().speed
                playerStats.defense = playerCSVStringSplited?.get(4)?.toInt() ?: Player().defense
                playerStats.healing = playerCSVStringSplited?.get(5)?.toInt() ?: Player().healing
                playerStats.statsPoint = playerCSVStringSplited?.get(6)?.toInt() ?: Player().statsPoint
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun onBackPressed() {
        //make it do nothing because you're not suppose to go back to anywhere in the main menu
    }

}
