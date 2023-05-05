package com.example.randomfight

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.Modifier
import com.example.randomfight.activities.DMGcalc
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
        val randomDifficulty = Random.nextInt(Wave*Wave)
        val enemyLevel = playerLevel + randomDifficulty
        val attack = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val hp: Int = Random.nextInt(enemyLevel*25 ,enemyLevel*100)
        val speed: Int = Random.nextInt(enemyLevel*10,enemyLevel*40)
        val defense: Int = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val healing: Int = Random.nextInt(enemyLevel*5, enemyLevel*20)
        val enemyStats = EnemyStats(attack,hp,speed,defense, healing)
        return enemyStats
    }

    fun enemyRandomlyChoseMove(): FightActivity.enemyMoveset {
        //will return action as a string like attack heal defend etc
        val listOfPossibleMove = listOf(FightActivity.enemyMoveset.ATTACK,
                                        FightActivity.enemyMoveset.DEFEND,
                                        FightActivity.enemyMoveset.SPEEDUP,
                                        FightActivity.enemyMoveset.HEAL,
                                        FightActivity.enemyMoveset.ATTACKUP,
                                        FightActivity.enemyMoveset.HEALUP)
        val result = listOfPossibleMove.random()
        return result
    }

    fun attack(attackerSpeed:Int,targetSpeed:Int,attackerATK:Int,targetDefense:Int):DMGcalc {
        var result = DMGcalc(false, 0)
        var possibleOutcomeStepOne = listOf(true,false)
        val randomizingOutCome = mutableListOf<Boolean>()
        var hitOrNot:Boolean
        if (attackerSpeed > targetSpeed) {
            for (outcome in possibleOutcomeStepOne) {
                val random = if(outcome == true) 5 else 1
                repeat(random){ randomizingOutCome.add(outcome) }
            }
        } else if (targetSpeed > attackerSpeed) {
            for (outcome in possibleOutcomeStepOne) {
                val random = if(outcome == true) 5 else 2
                repeat(random){ randomizingOutCome.add(outcome) }
            }
        } else {
            for (outcome in possibleOutcomeStepOne) {
                randomizingOutCome.add(outcome)
            }
        }

        hitOrNot = randomizingOutCome.random()
        if (hitOrNot == true) {
            result.hitOrNotHit = true
            if (attackerATK > targetDefense) {
                val powerGap = attackerATK - targetDefense
                val randomBonusDamage = Random.nextInt(powerGap)
                result.damageDealt = attackerATK + randomBonusDamage
            } else if (attackerATK < targetDefense) {
                val powerGap = targetDefense - attackerATK
                val randomReducedDamage = Random.nextInt(powerGap)
                result.damageDealt = attackerATK - randomReducedDamage
                if (result.damageDealt < 0) {
                    result.damageDealt = 0
                }
            }
        }

        return result
    }
}