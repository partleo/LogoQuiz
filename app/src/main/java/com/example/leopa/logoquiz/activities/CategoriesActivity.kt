package com.example.leopa.logoquiz.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.example.leopa.logoquiz.lists.Model
import com.example.leopa.logoquiz.R
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.lists.ListAdapter
import org.jetbrains.anko.doAsync

class CategoriesActivity : AppCompatActivity() {

    lateinit var listView : ListView

    private lateinit var db: DataBase
    private lateinit var turnPage: MediaPlayer
    private var soundsOff = false

    companion object {
        const val CATEGORY = "Category"

        const val CLOTHES = "Clothes"
        const val DRINKS = "Drinks"
        const val ENTERTAINMENT = "Entertainment"
        const val FOODS = "Foods"
        const val GAMES = "Games"
        const val MEDIA = "Media"
        const val MUSIC = "Music"
        const val SPORTS = "Sports"
        const val TECHNOLOGY = "Technology"
        const val TRANSPORTATION = "Transportation"
        const val VEHICLES = "Vehicles"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        db = DataBase.get(this)
        turnPage = MediaPlayer.create(this, R.raw.turnpage)
        doAsync { soundsOff = db.quizDao().getSoundsStatus() }

        listView = findViewById(R.id.listView)
        val list = mutableListOf<Model>()

        list.add(Model(CLOTHES, "Clothing Stores & Brands etc.", R.drawable.adidas))
        list.add(Model(DRINKS, "Cafes & Drink Brands etc.", R.drawable.pepsi))
        list.add(Model(ENTERTAINMENT, "Movies, TV Shows & Comics etc.", R.drawable.warnerbros))
        list.add(Model(FOODS, "Restaurants & Food Brands etc.", R.drawable.pringles))
        list.add(Model(GAMES, "Video & Board Games & Consoles etc.", R.drawable.playstation))
        list.add(Model(MEDIA, "Media Channels & Social Media Platforms etc.", R.drawable.facebook))
        list.add(Model(MUSIC, "Bands, Artists & Instrument Manufacturers etc.", R.drawable.rollingstones))
        list.add(Model(SPORTS, "Sport Teams & Associations etc.", R.drawable.olympicgames))
        list.add(Model(TECHNOLOGY, "Software & Hardware brands etc.", R.drawable.linux))
        list.add(Model(TRANSPORTATION, " Transportation equipments & services etc.", R.drawable.lufthansa))
        list.add(Model(VEHICLES, "Cars & Motorcycles etc.", R.drawable.volkswagen))


        listView.adapter = ListAdapter(this, R.layout.row, list)

        listView.setOnItemClickListener{parent, view, position, id ->

            if (!soundsOff) { turnPage.start() }

            val categoryIntent = Intent(this, CategoryActivity::class.java)

            /*
            when (position) {
                0 -> categoryIntent.putExtra(CATEGORY, BANDS)
                1 -> categoryIntent.putExtra(CATEGORY, CARS)
                2 -> categoryIntent.putExtra(CATEGORY, DRINKS)
                3 -> categoryIntent.putExtra(CATEGORY, FOODS)
                4 -> categoryIntent.putExtra(CATEGORY, SPORT_TEAMS)
                5 -> categoryIntent.putExtra(CATEGORY, VIDEO_GAMES)
                else -> Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
            }
            */

            categoryIntent.putExtra(CATEGORY, position+1)
            startActivity(categoryIntent)
        }

    }

    override fun onRestart() {
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onRestart()
    }
}