package com.example.randomfight.activities

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fightButton = findViewById<Button>(R.id.fightButton)
        val upgradeButton = findViewById<Button>(R.id.upgradeButton)
        val intent = getIntent()
        val player:Player = intent.getSerializableExtra("Player") as Player

        fightButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intent = Intent(this, FightActivity::class.java)
                if (player != Player())
                intent.putExtra("Player", player)
                startActivity(intent)
            } else {
                Toast.makeText(this, "check yer internet", Toast.LENGTH_LONG).show()
            }
        }

        upgradeButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intent = Intent(this, UpgradeActivity::class.java)
                if (player != Player())
                    intent.putExtra("Player", player)
                startActivity(intent)
            } else {
                Toast.makeText(this, "check yer internet", Toast.LENGTH_LONG).show()
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
