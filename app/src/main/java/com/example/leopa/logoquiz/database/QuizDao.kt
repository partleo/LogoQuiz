package com.example.leopa.logoquiz.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface QuizDao {

    @Query("SELECT * FROM levels WHERE id LIKE :categoryId")
    fun getLevels(categoryId: Int): LiveData<Levels>

    @Query("SELECT level FROM levels WHERE id LIKE :categoryId")
    fun getSavedLevel(categoryId: Int): Int

    @Query("SELECT current FROM levels WHERE id LIKE :categoryId")
    fun getSavedCurrentLevel(categoryId: Int): Int

    @Query("SELECT id FROM levels WHERE id LIKE :categoryId")
    fun getSavedLevelsId(categoryId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLevels(levels: Levels)




    @Query("SELECT COUNT(*) FROM guessedlogos")
    fun getAmountOfGuessedLogos(): Int

    @Query("SELECT name FROM guessedlogos WHERE name LIKE :logo")
    fun getLogo(logo: String): String
/*
    @Query("SELECT :logo FROM guessedlogos")
    fun getLogo(logo: String)
*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogo(logo: GuessedLogos)



    @Query("SELECT * FROM settings WHERE id LIKE 1")
    fun getSettings(): LiveData<Settings>

    @Query("SELECT vibrationOff FROM settings WHERE id LIKE 1")
    fun getVibrationStatus(): Boolean

    @Query("SELECT soundsOff FROM settings WHERE id LIKE 1")
    fun getSoundsStatus(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSettingsOnOff(status: Settings)

}