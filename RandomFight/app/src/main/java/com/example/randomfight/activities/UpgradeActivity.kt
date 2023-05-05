package com.example.randomfight.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.randomfight.R
import com.example.randomfight.entity_model.Player

class UpgradeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade)
        var playerstats = Player()
        updateView(playerstats)

        findViewById<TextView>(R.id.healthInUpgrade).setOnClickListener {
            upgradeButton(playerstats.health,0,0,0,0)
            playerstats = Player()
            updateView(playerstats)
        }

        findViewById<TextView>(R.id.attackInUpgrade).setOnClickListener {
            upgradeButton(0,10,0,0,0)
            playerstats = Player()
            updateView(playerstats)
        }

        findViewById<TextView>(R.id.defenseInUpgrade).setOnClickListener {
            upgradeButton(0,0,10,0,0)
            playerstats = Player()
            updateView(playerstats)
        }

        findViewById<TextView>(R.id.speedInUpgrade).setOnClickListener {
            upgradeButton(0,0,0,20,0)
            playerstats = Player()
            updateView(playerstats)
        }

        findViewById<TextView>(R.id.healingInUpgrade).setOnClickListener {
            upgradeButton(0,0,0,0,25)
            playerstats = Player()
            updateView(playerstats)
        }



    }

    @SuppressLint("SetTextI18n")
    fun updateView(playerStats:Player) {
        val playerHP = findViewById<TextView>(R.id.healthInUpgrade)
        val playerATK = findViewById<TextView>(R.id.attackInUpgrade)
        val playerDEF = findViewById<TextView>(R.id.defenseInUpgrade)
        val playerSP = findViewById<TextView>(R.id.speedInUpgrade)
        val playerHL = findViewById<TextView>(R.id.healingInUpgrade)
        val statsPoint = findViewById<TextView>(R.id.statsPoint)

        playerHP.text = "HP:${playerStats.health}"
        playerATK.text = "ATK:${playerStats.attack}"
        playerDEF.text = "DEF:${playerStats.defense}"
        playerSP.text = "SP:${playerStats.speed}"
        playerHL.text = "HL:${playerStats.healing}"
        statsPoint.text = "Stats Point:${playerStats.statsPoint}"
    }

    fun upgradeButton(healthUp:Int,atkUp:Int,defUp:Int,spUp:Int,hlUp:Int) {
        Player().health += healthUp
        Player().attack += atkUp
        Player().defense += defUp
        Player().speed += spUp
        Player().healing += hlUp

    }

}