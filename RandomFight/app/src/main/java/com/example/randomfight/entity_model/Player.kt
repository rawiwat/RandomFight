package com.example.randomfight.entity_model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Player : Serializable {
    var level:Int = 1
    var attack:Int = 10
    var health:Int = 50
    var speed:Int = 20
    var defense:Int = 10
    var healing:Int = 5
    var potential:Int = 1
    var statsPoint:Int = 0
    @PrimaryKey(autoGenerate = true)@NonNull
    var id:Int = 0

    constructor(){
        this.level
        this.attack
        this.health
        this.speed
        this.defense
        this.healing
        this.potential
        this.statsPoint
        this.id
    }

}