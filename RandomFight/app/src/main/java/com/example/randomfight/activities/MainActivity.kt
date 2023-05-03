package com.example.randomfight.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.randomfight.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fightButton = findViewById<Button>(R.id.fightButton)

        fightButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intent = Intent(this, FightActivity::class.java)
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
