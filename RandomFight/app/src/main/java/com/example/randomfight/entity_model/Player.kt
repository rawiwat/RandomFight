package com.example.randomfight.entity_model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Player : Serializable {
    var level:Int = 0
    var expUntilNextLevel:Int = 0
    var statsPoint:Int = 0
    var attack:Int = 0
    var health:Int = 0
    var speed:Int = 0
    var defense:Int = 0
    var healing:Int = 0
    var potential:Int = 0
    @PrimaryKey(autoGenerate = true)@NonNull
    var id:Int = 0

    constructor(){
        this.level = 0
        this.expUntilNextLevel = 0
        this.statsPoint = 0
        this.attack = 0
        this.health = 0
        this.speed = 0
        this.defense = 0
        this.healing = 0
        this.potential = 0
        this.id = 0
    }

}