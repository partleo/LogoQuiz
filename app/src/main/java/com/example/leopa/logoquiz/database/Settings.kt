package com.example.leopa.logoquiz.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey
    val id: Int,
    val vibrationOff: Boolean,
    val soundsOff: Boolean
){
    override fun toString(): String = "$vibrationOff"
}