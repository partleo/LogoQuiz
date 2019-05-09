package com.example.leopa.logoquiz.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(Levels::class), (GuessedLogos::class), (Settings::class)], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
    companion object {
        private var sInstance: DataBase? = null
        @Synchronized
        fun get(context: Context): DataBase {
            if (sInstance == null) {
                synchronized(DataBase::class) {
                    sInstance = Room.databaseBuilder(context.applicationContext, DataBase::class.java, "mysixthtestdatabase.db").build()
                }
            }
            return sInstance!!
        }
    }
}