package com.example.leopa.logoquiz.activities

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.example.leopa.logoquiz.lists.Model
import com.example.leopa.logoquiz.R
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.fragments.GuidelinesFragment
import com.example.leopa.logoquiz.fragments.SettingsFragment
import com.example.leopa.logoquiz.lists.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

    lateinit var listView : ListView
    private val viewGroup: ViewGroup? = null

    private val settingsFragment = SettingsFragment()
    private val guidelinesFragment = GuidelinesFragment()

    private lateinit var db: DataBase
    private lateinit var turnPage: MediaPlayer
    private var soundsOff = false

    companion object {
        val SETTINGS_FRAGMENT_TAG = "Settings"
        val GUIDELINES_FRAGMENT_TAG = "Guidelines"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DataBase.get(this)
        turnPage = MediaPlayer.create(this, R.raw.turnpage)
        doAsync { soundsOff = db.quizDao().getSoundsStatus() }
        clickListeners()
    }

    private fun clickListeners() {
        play.setOnClickListener {
            if (!soundsOff) { turnPage.start() }
            val categoriesIntent = Intent(this, CategoriesActivity::class.java)
            startActivity(categoriesIntent)
        }
        settings.setOnClickListener {
            //categoryId =  extras?.getInt(CategoriesActivity.CATEGORY)!!
            //val b = Bundle()
            //b.putInt(CategoriesActivity.CATEGORY, categoryId)
            //categoryFragment.arguments = b
            if (!soundsOff) { turnPage.start() }
            setupFragment(settingsFragment, SETTINGS_FRAGMENT_TAG)
        }
        guidelines.setOnClickListener {
            //categoryId =  extras?.getInt(CategoriesActivity.CATEGORY)!!
            //val b = Bundle()
            //b.putInt(CategoriesActivity.CATEGORY, categoryId)
            //categoryFragment.arguments = b
            if (!soundsOff) { turnPage.start() }
            setupFragment(guidelinesFragment, GUIDELINES_FRAGMENT_TAG)
        }
    }

    private fun setupFragment(fragment: Fragment, name: String) {
        main_content.visibility = LinearLayout.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity, fragment, name)
            .addToBackStack( "tag" )
            .commit()
    }


    override fun onBackPressed() {

        if (supportFragmentManager.findFragmentByTag(SETTINGS_FRAGMENT_TAG) != null
            && supportFragmentManager.findFragmentByTag(SETTINGS_FRAGMENT_TAG)!!.isVisible
            || supportFragmentManager.findFragmentByTag(GUIDELINES_FRAGMENT_TAG) != null
            && supportFragmentManager.findFragmentByTag(GUIDELINES_FRAGMENT_TAG)!!.isVisible){
            super.onBackPressed()
            main_content.visibility = LinearLayout.VISIBLE
            doAsync { soundsOff = db.quizDao().getSoundsStatus() }
        }
        else {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_close_app, viewGroup)
            builder.setView(dialogView)
                .setPositiveButton(R.string.yes) { _, _ ->
                    finish()
                }
                .setNegativeButton(R.string.no) { _, _ ->
                }.show()
        }
    }

}