package com.example.randomfight

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

    fun enemyRandomlyChoseMove():String{
        //will return action as a string like attack heal defend etc
        val listOfPossibleMove = listOf("Attack","Heal","Defend","SpeedBoost","AttackBoost","Attack","DefenseBoost","HealingBoost")
        val result = listOfPossibleMove.random()
        return result
    }
}