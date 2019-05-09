package com.example.leopa.logoquiz.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.leopa.logoquiz.R
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.database.Settings
import org.jetbrains.anko.doAsync

class SettingsFragment: Fragment() {

    private lateinit var sounds: Switch
    private lateinit var vibration: Switch


    private lateinit var db: DataBase
    private var soundsOff = false
    private var vibrationOff = false

    private lateinit var soundsOn: MediaPlayer
    private lateinit var vibrator: Vibrator

    private var updated = false

    companion object {
        const val VIBRATION_TIME_SHORT: Long = 100
        const val WAIT: Long = 200
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        db = DataBase.get(context!!)

        sounds = rootView.findViewById(R.id.sounds)
        vibration = rootView.findViewById(R.id.vibration)

        soundsOn = MediaPlayer.create(context!!, R.raw.soundson)
        vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        /*
        doAsync {
            soundsOff = db.quizDao().getSoundsStatus()
            vibrationOff = db.quizDao().getVibrationStatus()
        }
        */

        object : CountDownTimer(WAIT, WAIT) {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {
                updated = true
            }
        }.start()


        sounds.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                doAsync {
                    val vibrationStatus = db.quizDao().getVibrationStatus()
                    db.quizDao().insertSettingsOnOff(Settings(1, vibrationStatus, false))
                }
                if (updated) {
                    soundsOn.start()
                }
            }
            else {
                doAsync {
                    val vibrationStatus = db.quizDao().getVibrationStatus()
                    db.quizDao().insertSettingsOnOff(Settings(1, vibrationStatus, true))
                }
            }
        }

        vibration.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                doAsync {
                    val soundsStatus = db.quizDao().getSoundsStatus()
                    db.quizDao().insertSettingsOnOff(Settings(1, false, soundsStatus))
                }
                if (updated) {
                    vibrator.vibrate(VIBRATION_TIME_SHORT)
                }
            }
            else {
                doAsync {
                    val soundsStatus = db.quizDao().getSoundsStatus()
                    db.quizDao().insertSettingsOnOff(Settings(1, true, soundsStatus))
                }
            }
        }

        createObserver()

        return rootView
    }

    private fun createObserver() {
        db.quizDao().getSettings().observe(this , Observer { it ->
            if (it != null){
                soundsOff = it.soundsOff
                vibrationOff = it.vibrationOff
                sounds.isChecked = !soundsOff
                vibration.isChecked = !vibrationOff
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        updated = false
    }
}