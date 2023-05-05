package com.example.randomfight.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.randomfight.R
import com.example.randomfight.RNG
import com.example.randomfight.api.randomEnemyApi
import com.example.randomfight.entity_model.Enemy
import com.example.randomfight.entity_model.EnemyStats
import com.example.randomfight.entity_model.Player
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class FightActivity : AppCompatActivity() {

    enum class currentTurn {
        PLAYER_TURN,
        ENEMY_TURN
    }

    enum class enemyMoveset{
        ATTACK,
        DEFEND,
        SPEEDUP,
        HEAL,
        ATTACKUP,
        HEALUP
    }

    val playerStats = Player()
    var wave = 1
    var enemyStats = RNG().getRandomEnemyStats(1,wave)
    var levelAtStartOfFight = playerStats.level
    lateinit var turn:currentTurn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        findViewById<Button>(R.id.backButton).setOnClickListener{
            goBackConfirmation()
        }
        initialize(playerStats,enemyStats)
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
                        val enemyName = findViewById<TextView>(R.id.enemyName)
                        enemyName.text = it.name
                        executioner.execute {
                            val imageUrl = it.img
                            try {
                                val loading = findViewById<ProgressBar>(R.id.enemyImageLoading)
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
                                    loading.visibility = GONE
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

    fun goBackConfirmation() {
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

    private fun whoGoesFirst(playerSpeed:Int,enemySpeed:Int):currentTurn {
        val possibilities = listOf(currentTurn.PLAYER_TURN,currentTurn.ENEMY_TURN)
        lateinit var result:currentTurn
        if (playerSpeed > enemySpeed) {
            result = possibilities[0]
        } else if (enemySpeed > playerSpeed) {
            result = possibilities[1]
        } else {
            result = possibilities.random()
        }

        return result
    }

    private fun playerMove () {
        val attackButton = findViewById<Button>(R.id.attackButton)
        val defendButton = findViewById<Button>(R.id.defendButton)
        val speedUpButton = findViewById<Button>(R.id.speedButton)
        val healButton = findViewById<Button>(R.id.healButton)
        val attackUpButton = findViewById<Button>(R.id.attackBoostButton)
        val healUpButton = findViewById<Button>(R.id.healingBoostButton)

        buttonViewOn()
        attackButton.setOnClickListener {
            buttonViewOff()
            playerAttack()
            //updateView(playerStats, enemyStats)
        }

        defendButton.setOnClickListener {
            buttonViewOff()
            val preBuffedDefense = playerStats.defense
            val buffedDefense = playerStats.defense * 2
            playerStats.defense = buffedDefense
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Your Defense Goes Up by ${buffedDefense - preBuffedDefense}")
        }

        speedUpButton.setOnClickListener {
            buttonViewOff()
            val preBuffedSpeed = playerStats.speed
            val buffedSpeed = playerStats.speed * 2
            playerStats.speed = buffedSpeed
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Your Speed Goes Up by ${buffedSpeed - preBuffedSpeed}")
        }

        healButton.setOnClickListener {
            buttonViewOff()
            val currentHealth = playerStats.health
            val maxHealthView = findViewById<TextView>(R.id.playerMaxHp)
            val maxHealth = maxHealthView.text.toString().toInt()
            val damage = maxHealth - currentHealth
            if (damage >= playerStats.healing) {
                playerStats.health += playerStats.healing
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("You healed ${playerStats.healing} Health!")
            } else if (damage != 0) {
                playerStats.health = maxHealth
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("You healed ${damage} Health!")
            } else if (currentHealth == maxHealth) {
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("Your Health is already full!")
            }
        }

        attackUpButton.setOnClickListener {
            buttonViewOff()
            val preBuffedAttack = playerStats.attack
            val buffedAttack = playerStats.attack * 2
            playerStats.attack = buffedAttack
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Your Attack Power Goes Up by ${buffedAttack - preBuffedAttack}")
        }

        healUpButton.setOnClickListener {
            buttonViewOff()
            val preBuffedHealing = playerStats.healing
            val buffedHealing = playerStats.healing * 2
            playerStats.healing = buffedHealing
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Your Healing Power Goes Up by ${buffedHealing - preBuffedHealing}")
        }

    }

    private fun enemyMove() {

        val enemyRandomMove = RNG().enemyRandomlyChoseMove()

        println(enemyRandomMove)

        if (enemyRandomMove == enemyMoveset.ATTACK){
            enemyAttack()
            updateView(playerStats, enemyStats)

        } else if (enemyRandomMove == enemyMoveset.DEFEND){
            val preBuffedDefense = enemyStats.defense
            val buffedDefense = enemyStats.defense * 2
            enemyStats.defense = buffedDefense
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Enemy's Defense Goes Up by ${buffedDefense - preBuffedDefense}")

        } else if (enemyRandomMove == enemyMoveset.SPEEDUP){
            val preBuffedSpeed = enemyStats.speed
            val buffedSpeed = enemyStats.speed * 2
            enemyStats.speed = buffedSpeed
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Enemy's Speed Goes Up by ${buffedSpeed - preBuffedSpeed}")

        } else if (enemyRandomMove == enemyMoveset.HEAL){
            val currentHealth = enemyStats.health
            val maxHealthView = findViewById<TextView>(R.id.enemyMaxHp)
            val maxHealth = maxHealthView.text.toString().toInt()
            val damage = maxHealth - currentHealth

            if (damage >= enemyStats.healing) {
                enemyStats.health += enemyStats.healing
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("Enemy healed ${enemyStats.healing} Health!")
            } else if (damage != 0) {
                enemyStats.health = maxHealth
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("Enemy healed ${damage} Health!")
            } else if (currentHealth == maxHealth) {
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("Enemy healed but it's Health is already full!")
            }

        } else if (enemyRandomMove == enemyMoveset.ATTACKUP){
            val preBuffedAttack = enemyStats.attack
            val buffedAttack = enemyStats.attack * 2
            enemyStats.attack = buffedAttack
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Enemy's Attack Power Goes Up by ${buffedAttack - preBuffedAttack}")
        } else if (enemyRandomMove == enemyMoveset.HEALUP){
            val preBuffedHealing = enemyStats.healing
            val buffedHealing = enemyStats.healing * 2
            enemyStats.healing = buffedHealing
            updateView(playerStats,enemyStats)
            showToastThenChangeTurn("Enemy's Healing Power Goes Up by ${buffedHealing - preBuffedHealing}")
        }
    }

    private fun initialize (playerStats:Player,enemyStats: EnemyStats) {
        Toast.makeText(this,"a New Enemy Arrived!",Toast.LENGTH_SHORT).show()
        getAllDigimons()
        val playerHealthView = findViewById<TextView>(R.id.playerHp)
        val playerMaxHealthView = findViewById<TextView>(R.id.playerMaxHp)
        val playerAttackView = findViewById<TextView>(R.id.playerAP)
        val playerDefenseView = findViewById<TextView>(R.id.playerDefense)
        val playerSpeedView = findViewById<TextView>(R.id.playerSpeed)
        val playerHealingView = findViewById<TextView>(R.id.playerHealing)
        val playerLevelView = findViewById<TextView>(R.id.playerLevel)

        playerHealthView.text = playerStats.health.toString()
        playerMaxHealthView.text = playerStats.health.toString()
        playerAttackView.text = playerStats.attack.toString()
        playerDefenseView.text = playerStats.defense.toString()
        playerSpeedView.text = playerStats.speed.toString()
        playerHealingView.text = playerStats.healing.toString()
        playerLevelView.text = " LV:" + "${playerStats.level}"

        val firstEnemyHealthView = findViewById<TextView>(R.id.enemyHp)
        val firstEnemyMaxHealthView = findViewById<TextView>(R.id.enemyMaxHp)
        val firstEnemyAttackView = findViewById<TextView>(R.id.enemyAP)
        val firstEnemyDefenseView = findViewById<TextView>(R.id.enemyDefense)
        val firstEnemySpeedView = findViewById<TextView>(R.id.enemySpeed)
        val firstEnemyHealingView = findViewById<TextView>(R.id.enemyHealing)

        firstEnemyHealthView.text = enemyStats.health.toString()
        firstEnemyMaxHealthView.text = enemyStats.health.toString()
        firstEnemyAttackView.text = enemyStats.attack.toString()
        firstEnemyDefenseView.text = enemyStats.defense.toString()
        firstEnemySpeedView.text = enemyStats.speed.toString()
        firstEnemyHealingView.text = enemyStats.healing.toString()

        turn = whoGoesFirst(playerStats.speed,enemyStats.speed)
        val turnView = findViewById<TextView>(R.id.turn)
        if (turn == currentTurn.PLAYER_TURN){
            turnView.text = "Your Turn"
            buttonViewOn()
            Handler().postDelayed({playerMove()},3000)
        } else {
            turnView.text = "Enemy Turn"
            buttonViewOff()
            Handler().postDelayed({enemyMove()},3000)
        }

    }

    private fun updateView (playerStats:Player,enemyStats: EnemyStats) {
        val playerHealthView = findViewById<TextView>(R.id.playerHp)
        val playerAttackView = findViewById<TextView>(R.id.playerAP)
        val playerDefenseView = findViewById<TextView>(R.id.playerDefense)
        val playerSpeedView = findViewById<TextView>(R.id.playerSpeed)
        val playerHealingView = findViewById<TextView>(R.id.playerHealing)
        val playerLevelView = findViewById<TextView>(R.id.playerLevel)

        playerHealthView.text = playerStats.health.toString()
        playerAttackView.text = playerStats.attack.toString()
        playerDefenseView.text = playerStats.defense.toString()
        playerSpeedView.text = playerStats.speed.toString()
        playerHealingView.text = playerStats.healing.toString()
        playerLevelView.text = " LV:" + "${playerStats.level}"

        val enemyHealthView = findViewById<TextView>(R.id.enemyHp)
        val enemyAttackView = findViewById<TextView>(R.id.enemyAP)
        val enemyDefenseView = findViewById<TextView>(R.id.enemyDefense)
        val enemySpeedView = findViewById<TextView>(R.id.enemySpeed)
        val enemyHealingView = findViewById<TextView>(R.id.enemyHealing)

        enemyHealthView.text = enemyStats.health.toString()
        enemyAttackView.text = enemyStats.attack.toString()
        enemyDefenseView.text = enemyStats.defense.toString()
        enemySpeedView.text = enemyStats.speed.toString()
        enemyHealingView.text = enemyStats.healing.toString()
    }

    fun playerAttack() {
        val damage = RNG().attack(
            playerStats.speed,
            enemyStats.speed,
            playerStats.attack,
            enemyStats.defense
        )
        if (damage.hitOrNotHit == true && damage.damageDealt > 0 && damage.damageDealt < enemyStats.health) {
            enemyStats.health -= damage.damageDealt
            updateView(playerStats, enemyStats)
            showToastThenChangeTurn("${damage.damageDealt} DMG was dealt!")
        } else if (damage.hitOrNotHit == false) {
            showToastThenChangeTurn("The Attack Missed!")
        } else if (damage.damageDealt == 0){
            showToastThenChangeTurn("The Enemy was too tough no Damage was Dealt!")
        } else if (damage.damageDealt >= enemyStats.health){
            findViewById<TextView>(R.id.enemyHp).text = "0"
            Toast.makeText(this, "Enemy Was Defeated", Toast.LENGTH_SHORT).show()
            //updateView(playerStats, enemyStats)
            wave++
            playerStats.level += wave
            enemyStats = RNG().getRandomEnemyStats(playerStats.level, wave)
            Handler().postDelayed({ initialize(playerStats, enemyStats) }, 3000)
        }
    }

    fun enemyAttack() {
        val damage = RNG().attack(
            enemyStats.speed,
            playerStats.speed,
            enemyStats.attack,
            playerStats.defense
        )
        if (damage.hitOrNotHit == true && damage.damageDealt > 0) {
            if (damage.damageDealt < playerStats.health) {
                playerStats.health -= damage.damageDealt
                updateView(playerStats, enemyStats)
                showToastThenChangeTurn("${damage.damageDealt} DMG was dealt!")
            } else {
                playerStats.health = 0
                Toast.makeText(this,"You Got Defeated!",Toast.LENGTH_SHORT).show()
                Toast.makeText(this,"GAME OVER!",Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    gameOverManGameOver()
                }, 3000)
            }
        } else if (damage.hitOrNotHit == false) {
            showToastThenChangeTurn("You Dodged, The Attack Missed!")
        } else {
            showToastThenChangeTurn("You are too tough no Damage was Dealt!")
        }
    }

    fun buttonViewOff() {
        val attackButton = findViewById<Button>(R.id.attackButton)
        val defendButton = findViewById<Button>(R.id.defendButton)
        val speedUpButton = findViewById<Button>(R.id.speedButton)
        val healButton = findViewById<Button>(R.id.healButton)
        val attackUpButton = findViewById<Button>(R.id.attackBoostButton)
        val healUpButton = findViewById<Button>(R.id.healingBoostButton)

        if (attackButton.visibility == VISIBLE) {
            attackButton.visibility = GONE
        }

        if (defendButton.visibility == VISIBLE) {
            defendButton.visibility = GONE
        }

        if (speedUpButton.visibility == VISIBLE) {
            speedUpButton.visibility = GONE
        }

        if (healButton.visibility == VISIBLE) {
            healButton.visibility = GONE
        }

        if (attackUpButton.visibility == VISIBLE) {
            attackUpButton.visibility = GONE
        }

        if (healUpButton.visibility == VISIBLE) {
            healUpButton.visibility = GONE
        }
    }

    fun buttonViewOn() {
        val attackButton = findViewById<Button>(R.id.attackButton)
        val defendButton = findViewById<Button>(R.id.defendButton)
        val speedUpButton = findViewById<Button>(R.id.speedButton)
        val healButton = findViewById<Button>(R.id.healButton)
        val attackUpButton = findViewById<Button>(R.id.attackBoostButton)
        val healUpButton = findViewById<Button>(R.id.healingBoostButton)

        if (attackButton.visibility == GONE) {
            attackButton.visibility = VISIBLE
        }

        if (defendButton.visibility == GONE) {
            defendButton.visibility = VISIBLE
        }

        if (speedUpButton.visibility == GONE) {
            speedUpButton.visibility = VISIBLE
        }

        if (healButton.visibility == GONE) {
            healButton.visibility = VISIBLE
        }

        if (attackUpButton.visibility == GONE) {
            attackUpButton.visibility = VISIBLE
        }

        if (healUpButton.visibility == GONE) {
            healUpButton.visibility = VISIBLE
        }
    }

    fun shiftTurn() {
        val turnView = findViewById<TextView>(R.id.turn)
        if (turn == currentTurn.PLAYER_TURN) {
            turn = currentTurn.ENEMY_TURN
            turnView.text = "Enemy Turn"
            //shiftButtonView()
            enemyMove()
        } else {
            turn = currentTurn.PLAYER_TURN
            turnView.text = "Your Turn"
            //shiftButtonView()
            playerMove()
        }
    }

    fun showToastThenChangeTurn(toastText:String){
        val toasty = Toast.makeText(this, toastText, Toast.LENGTH_SHORT)
        toasty.show()
        Handler().postDelayed({
            shiftTurn()
        }, 3000)
    }

     fun gameOverManGameOver(){
         val levelGained = playerStats.level - levelAtStartOfFight
         val statsPointGain = levelGained * 2
         Player().level += levelGained
         Player().statsPoint += statsPointGain

         val gameOverMenu = findViewById<ImageView>(R.id.gameOverMenu)
         val gameOverText = findViewById<TextView>(R.id.gameOverMenuText)
         val gameOverButton = findViewById<Button>(R.id.gameOverButton)
         gameOverMenu.visibility = VISIBLE
         gameOverText.visibility = VISIBLE
         gameOverButton.visibility = VISIBLE
         gameOverText.text = "You Gain $levelGained Level and $statsPointGain Stats Point Go Back to Main Menu to Upgrade then Fight Again?"
         gameOverButton.setOnClickListener {
             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)
         }
     }
}