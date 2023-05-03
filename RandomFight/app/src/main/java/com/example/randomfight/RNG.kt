package com.example.randomfight

import android.widget.Toast
import androidx.compose.ui.Modifier
import com.example.randomfight.activities.FightActivity
import com.example.randomfight.entity_model.Enemy
import com.example.randomfight.entity_model.EnemyStats
import kotlin.random.Random

class RNG {
    fun getRandomDigimon(digimonList:List<Enemy>):Enemy {
        val randomDigimon = (0 until digimonList.size).random()
        val result = digimonList[randomDigimon]
        return result
    }

    fun getRandomEnemyStats(playerLevel:Int, Wave:Int): EnemyStats {
        val randomDifficulty = Random.nextInt(Wave*2)
        val enemyLevel = playerLevel + randomDifficulty
        val attack = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val hp: Int = Random.nextInt(enemyLevel*25 ,enemyLevel*100)
        val speed: Int = Random.nextInt(enemyLevel*10,enemyLevel*40)
        val defense: Int = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val healing: Int = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val enemyStats = EnemyStats(attack,hp,speed,defense, healing)
        return enemyStats
    }

    fun enemyRandomlyChoseMove():String {
        //will return action as a string like attack heal defend etc
        val listOfPossibleMove = listOf("Attack","Heal","SpeedBoost","AttackBoost","DefenseBoost","HealingBoost")
        val result = listOfPossibleMove.random()
        return result
    }

    fun attack(attackerSpeed:Int,targetSpeed:Int,attackerATK:Int,targetDefense:Int):Int? {
        var damageDealt:Int? = null
        var possibleOutcomeStepOne = listOf("hit","not hit")
        val randomizingOutCome = mutableListOf<String>()
        var hitOrNot:String = ""
        if (attackerSpeed > targetSpeed) {
            for (outcome in possibleOutcomeStepOne) {
                val random = if(outcome == "hit") 5 else 2
                repeat(random){ randomizingOutCome.add(outcome) }
            }
        } else if (targetSpeed > attackerSpeed) {
            for (outcome in possibleOutcomeStepOne) {
                val random = if(outcome == "not hit") 4 else 3
                repeat(random){ randomizingOutCome.add(outcome) }
            }
        } else {
            for (outcome in possibleOutcomeStepOne) {
                randomizingOutCome.add(outcome)
            }
        }

        hitOrNot = randomizingOutCome.random()
        if (hitOrNot == "not hit") {
            Toast.makeText(FightActivity(), "The Attack Missed!", Toast.LENGTH_SHORT).show()
        } else if (attackerATK > targetDefense){
            val powerGap = attackerATK - targetDefense
            val randomBonusDamage = Random.nextInt(powerGap*3)
            damageDealt = attackerATK + randomBonusDamage
        } else if (attackerATK == targetDefense) {
            damageDealt = attackerATK
        }

        return damageDealt
    }
}