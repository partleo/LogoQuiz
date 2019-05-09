package com.example.leopa.logoquiz.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Levels(
    @PrimaryKey
    val id: Int,
    val level: Int,
    val current: Int
){
    override fun toString(): String = "$level"
}