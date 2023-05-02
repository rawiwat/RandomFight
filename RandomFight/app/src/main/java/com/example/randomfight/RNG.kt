package com.example.randomfight

class RNG {
    fun getRandomDigimon(digimonList:MutableList<String?>):String? {
        val randomDigimon = (0 until digimonList.size).random()
        val result = digimonList[randomDigimon]
        return result
    }

    fun getRandomAttackpoint() {

    }

    fun getRandomHealthPoint(){

    }
}