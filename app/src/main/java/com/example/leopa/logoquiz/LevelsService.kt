package com.example.leopa.logoquiz

/*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import org.jetbrains.anko.doAsync

class LevelsService : Service() {

    private lateinit var db: DataBase
    private val mBinder = LevelsBinder()
    private var level: Int = 0
    private var currentLevel: Int = 1
    private val currentCategoryId: Int = 1
    private lateinit var mainActivity: MainActivity

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        db = DataBase.get(this)
        getSavedSteps()
        mainActivity = MainActivity()
    }

    inner class LevelsBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: LevelsService
            get() = this@LevelsService
    }

    private fun saveLevels(id) {
        doAsync {
            db.levelsDao().insertLevels(Levels(currentCategoryId, level, currentLevel))
        }
    }

    private fun getSavedSteps() {
        doAsync {
            db.levelsDao()
        }
    }

    fun resetSteps() {
        doAsync {
            stepCount = 0
            db.myDao().insertSteps(Levels(currentStepsId, stepCount))

        }
    }


}*/