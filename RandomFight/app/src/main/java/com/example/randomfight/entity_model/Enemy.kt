package com.example.randomfight.entity_model

import java.io.Serializable

class Enemy : Serializable{
    var name:String? = null
    var img:String? = null

    constructor(){
        this.name = null
        this.img = null
    }
}