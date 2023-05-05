package com.example.randomfight.entity_model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Player : Serializable {
    /*note: this stats is only for testing the real start of game stats should be
    * health:50
      attack:10
      speed:20
      def:10
      healing:25
      statsPoint:0
    * */
    var level:Int = 1
    var attack:Int = 10
    var health:Int = 50
    var speed:Int = 20
    var defense:Int = 10
    var healing:Int = 25
    //var potential:Int = 1
    var statsPoint:Int = 100
    @PrimaryKey(autoGenerate = true)@NonNull
    var id:Int = 0

    constructor(){
        this.level
        this.attack
        this.health
        this.speed
        this.defense
        this.healing
    //    this.potential
        this.statsPoint
        this.id
    }

}