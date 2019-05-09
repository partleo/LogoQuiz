package com.example.leopa.logoquiz.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GuessedLogos(
    @PrimaryKey
    val id: Int,
    val name: String
)