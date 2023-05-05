package com.example.randomfight.entity_model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class Player(var level:Int = 1,
                  var attack:Int = 10,
                  var health:Int = 50,
                  var speed:Int = 20,
                  var defense:Int = 10,
                  var healing:Int = 25,
                  var statsPoint:Int = 2) : Parcelable {

    constructor(parcel: Parcel) : this() {
        level = parcel.readInt()
        attack = parcel.readInt()
        health = parcel.readInt()
        speed = parcel.readInt()
        defense = parcel.readInt()
        healing = parcel.readInt()
        statsPoint = parcel.readInt()
    }

    constructor(CSVString: String) : this() {
        val attribute = CSVString.split(",")
        this.level = attribute[0].toInt()
        this.attack = attribute[1].toInt()
        this.health = attribute[3].toInt()
        this.speed = attribute[4].toInt()
        this.defense = attribute[5].toInt()
        this.healing = attribute[6].toInt()
        this.statsPoint = attribute[7].toInt()
    }

    fun toCSVString():String {
        return "$level,$attack,$health,$speed,$defense,$healing,$statsPoint"
    }

    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
    }

    override fun writeToParcel(player: Parcel, flags: Int) {
        player.writeInt(level)
        player.writeInt(attack)
        player.writeInt(health)
        player.writeInt(speed)
        player.writeInt(defense)
        player.writeInt(healing)
        player.writeInt(statsPoint)
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }

}