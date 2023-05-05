package com.example.randomfight.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.randomfight.R
import com.example.randomfight.entity_model.Player

class UpgradeActivity : AppCompatActivity() {

    val playerStats = Player()
    var summitedStats = Player()
    val playerStatsBeforeUpgradeActivity = playerStats
    var progressSave = false
    lateinit var playerCSVString:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade)

        println(playerStats)
        updateView(playerStats)

        findViewById<Button>(R.id.healthUpgradeButton).setOnClickListener {
            upgradeButton(playerStats,playerStats.health/2,0,0,0,0)
            println(playerStats)
            updateView(playerStats)
        }

        findViewById<Button>(R.id.attackUpgradeButton).setOnClickListener {
            upgradeButton(playerStats,0,playerStats.attack/2,0,0,0)
            println(playerStats)
            updateView(playerStats)
        }

        findViewById<Button>(R.id.defenseUpgradeButton).setOnClickListener {
            upgradeButton(playerStats,0,0,playerStats.defense/2,0,0)
            println(playerStats)
            updateView(playerStats)
        }

        findViewById<Button>(R.id.speedUpgradeButton).setOnClickListener {
            upgradeButton(playerStats,0,0,0,playerStats.speed/2,0)
            println(playerStats)
            updateView(playerStats)
        }

        findViewById<Button>(R.id.healingUpgradeButton).setOnClickListener {
            upgradeButton(playerStats,0,0,0,0,playerStats.healing/2)
            println(playerStats)
            updateView(playerStats)
        }

        findViewById<Button>(R.id.summitUpgradeButton).setOnClickListener {
            progressSave = true
            summitedStats = playerStats
            Toast.makeText(this, "Upgrade Saved", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.backButtonInUpgrade).setOnClickListener {
            goBackConfirmation()
        }

    }

    override fun onBackPressed() {
        goBackConfirmation()
    }
    fun updateView(playerstats:Player) {
        val playerHP = findViewById<TextView>(R.id.healthInUpgrade)
        val playerATK = findViewById<TextView>(R.id.attackInUpgrade)
        val playerDEF = findViewById<TextView>(R.id.defenseInUpgrade)
        val playerSP = findViewById<TextView>(R.id.speedInUpgrade)
        val playerHL = findViewById<TextView>(R.id.healingInUpgrade)
        val statsPoint = findViewById<TextView>(R.id.statsPoint)

        playerHP.text = playerstats.health.toString()
        playerATK.text = playerstats.attack.toString()
        playerDEF.text = playerstats.defense.toString()
        playerSP.text = playerstats.speed.toString()
        playerHL.text = playerstats.healing.toString()
        statsPoint.text = playerstats.statsPoint.toString()
    }

    fun upgradeButton(playerstats: Player, healthUp:Int,atkUp:Int,defUp:Int,spUp:Int,hlUp:Int) {
        if (playerstats.statsPoint >= 1) {
            playerstats.statsPoint--
            playerstats.health += healthUp
            playerstats.attack += atkUp
            playerstats.defense += defUp
            playerstats.speed += spUp
            playerstats.healing += hlUp
        } else {
            Toast.makeText(this,"no stats point left!",Toast.LENGTH_SHORT).show()
        }
    }

    fun goBackConfirmation() {
        val backPressedMenu = findViewById<ImageView>(R.id.backPressMenuInUpgrade)
        val backPressedMenuText = findViewById<TextView>(R.id.backPressMenuTextInUpgrade)
        val backPressedYes = findViewById<Button>(R.id.yesBackPressedMenuInUpgrade)
        val backPressedNo = findViewById<Button>(R.id.NoBackPressedMenuInUpgrade)
        backPressedMenu.visibility = View.VISIBLE
        backPressedMenuText.visibility = View.VISIBLE
        backPressedYes.visibility = View.VISIBLE
        backPressedNo.visibility = View.VISIBLE
        backPressedYes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if (progressSave == true) {
                val playerCSVString = summitedStats.toCSVString()
                intent.putExtra("Player", playerCSVString)
                setResult(Activity.RESULT_OK, intent)
                startActivityForResult(intent, 1)
            } else {
                val playerCSVString = playerStatsBeforeUpgradeActivity.toCSVString()
                intent.putExtra("Player", playerCSVString)
                setResult(Activity.RESULT_OK, intent)
                startActivityForResult(intent, 1)
            }
        }

        backPressedNo.setOnClickListener {
            backPressedMenu.visibility = View.GONE
            backPressedMenuText.visibility = View.GONE
            backPressedYes.visibility = View.GONE
            backPressedNo.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
                playerCSVString = data?.getStringExtra("Player").toString()
            }
            MainActivity().getPlayerStatsFromCSVString(playerCSVString,playerStats)
        }

}