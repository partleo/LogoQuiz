package com.example.leopa.logoquiz.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
import com.example.leopa.logoquiz.*
import com.example.leopa.logoquiz.activities.CategoriesActivity
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.database.Levels
import com.example.leopa.logoquiz.lists.LogoNames
import com.example.leopa.logoquiz.lists.Logos
import org.jetbrains.anko.doAsync
import kotlin.collections.ArrayList


class CategoryFragment: Fragment() {

    private lateinit var list: MutableList<ImageView>
    private lateinit var logo1: ImageView
    private lateinit var logo2: ImageView
    private lateinit var logo3: ImageView
    private lateinit var logo4: ImageView
    private lateinit var logo5: ImageView
    private lateinit var logo6: ImageView
    private lateinit var logo7: ImageView
    private lateinit var logo8: ImageView
    private lateinit var logo9: ImageView
    private lateinit var previous: ImageView
    private lateinit var next: ImageView
    private var currentLevel = 1
    private lateinit var currentCategory: String
    private var categoryId = 1
    private lateinit var level: TextView

    private lateinit var db: DataBase

    private lateinit var turnPage: MediaPlayer
    private lateinit var cannotTurnPage: MediaPlayer
    private lateinit var popUp: MediaPlayer

    private var soundsOff = false

    // Store instance variables
    //private var title: String? = null
    //private var page: Int = 0
    //private var category: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)
        turnPage = MediaPlayer.create(context!!, R.raw.turnpage)
        cannotTurnPage = MediaPlayer.create(context!!, R.raw.cannotturnpage)
        popUp = MediaPlayer.create(context!!, R.raw.popup)
        db = DataBase.get(context!!)




        clickListeners(rootView)
        level = rootView.findViewById(R.id.level)
        //level.text = "1"
        level.text = getString(R.string.level, 1)

        list = mutableListOf(
            logo1, logo2, logo3, logo4, logo5, logo6, logo7, logo8, logo9
        )



        checkCategory()
        getLevel()

        createObserver()

        doAsync { soundsOff = db.quizDao().getSoundsStatus() }
        //continueLevel()
        return rootView
    }

    private fun sharedElementTransition(iv: ImageView) {

        val detailFragment = DetailFragment()
        val b = Bundle()
        b.putString("Shared element", iv.transitionName)

        val bitmap = (iv.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val logo = stream.toByteArray()
        b.putByteArray("Logo", logo)
        b.putString("Name", iv.tag.toString())
        b.putInt("Category", categoryId)

        detailFragment.arguments = b

        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment, "Detail")
            .addSharedElement(iv, iv.transitionName)
            .addToBackStack( "tag" )
            .commit()

    }

    private fun clickListeners(rootView: View) {
        logo1 = rootView.findViewById(R.id.logo1)
        logo2 = rootView.findViewById(R.id.logo2)
        logo3 = rootView.findViewById(R.id.logo3)
        logo4 = rootView.findViewById(R.id.logo4)
        logo5 = rootView.findViewById(R.id.logo5)
        logo6 = rootView.findViewById(R.id.logo6)
        logo7 = rootView.findViewById(R.id.logo7)
        logo8 = rootView.findViewById(R.id.logo8)
        logo9 = rootView.findViewById(R.id.logo9)

        previous = rootView.findViewById(R.id.previous_level)
        next = rootView.findViewById(R.id.next_level)

        logo1.setOnClickListener {
            sharedElementTransition(logo1)
            if (!soundsOff) { popUp.start() }
        }
        logo2.setOnClickListener {
            sharedElementTransition(logo2)
            if (!soundsOff) { popUp.start() }
        }
        logo3.setOnClickListener {
            sharedElementTransition(logo3)
            if (!soundsOff) { popUp.start() }
        }
        logo4.setOnClickListener {
            sharedElementTransition(logo4)
            if (!soundsOff) { popUp.start() }
        }
        logo5.setOnClickListener {
            sharedElementTransition(logo5)
            if (!soundsOff) { popUp.start() }
        }
        logo6.setOnClickListener {
            sharedElementTransition(logo6)
            if (!soundsOff) { popUp.start() }
        }
        logo7.setOnClickListener {
            sharedElementTransition(logo7)
            if (!soundsOff) { popUp.start() }
        }
        logo8.setOnClickListener {
            sharedElementTransition(logo8)
            if (!soundsOff) { popUp.start() }
        }
        logo9.setOnClickListener {
            sharedElementTransition(logo9)
            if (!soundsOff) { popUp.start() }
        }


        previous.setOnClickListener {
            changeLevel(false)
        }
        next.setOnClickListener {
            changeLevel(true)
        }
    }

    private fun getLevel() {
        doAsync {
            val levels = db.quizDao().getLevels(categoryId).value
            if (levels != null) { db.quizDao().insertLevels(levels) }
            else { db.quizDao().insertLevels(Levels(categoryId, 1, currentLevel)) }
        }
    }

    private fun changeLevel(next: Boolean) {
        if (next) {
            //turnPage.start()
            if (!soundsOff) { turnPage.start() }
            currentLevel += 1
            level.text = getString(R.string.level, currentLevel)
            doAsync {
                val levelOpened = db.quizDao().getLevels(categoryId).value?.level!!
                db.quizDao().insertLevels(
                    Levels(
                        categoryId,
                        levelOpened,
                        currentLevel
                    )
                )
            }
        }
        else {
            if (currentLevel > 1) {
                //turnPage.start()
                if (!soundsOff) { turnPage.start() }
                currentLevel -= 1
                level.text = getString(R.string.level, currentLevel)
                doAsync {
                    val levelOpened = db.quizDao().getLevels(categoryId).value?.level!!
                    db.quizDao().insertLevels(
                        Levels(
                            categoryId,
                            levelOpened,
                            currentLevel
                        )
                    )
                }
            }
            else {
                //cannotTurnPage.start()
                if (!soundsOff) {
                    cannotTurnPage.start()
                }
            }
        }

        checkCategory()
    }

/*
    private fun checkLevel(category: Int) {

        Log.d("Tää", "check")

        doAsync {
            Log.d("Tää", "doAsync")
            var level = db.quizDao().getSavedLevel(category)
            var current = db.quizDao().getSavedCurrentLevel(category)
            if (level == 0 || current == 0) {
                level = 1
                current = 1
            }
            Log.d("Tää", "level: $level and current: $current")

            db.quizDao().insertLevels(Levels(category, level, current))
        }

        //get level from room database


    }
*/


    var progress = false
    var continuationLevel = 1

    private fun checkCategory() {
        /*
        arguments?.let {
            currentCategory = it.getString(CategoriesActivity.CATEGORY)!!
        }
        getLogos(currentCategory)
        */
        arguments?.let {
            categoryId = it.getInt(CategoriesActivity.CATEGORY)

            /*
            progress = it.getBoolean("Progress")
            if (progress) {

                arguments!!.remove("Progress")

                doAsync {
                    //continuationLevel = db.quizDao().getLevels(categoryId).value!!.level
                    continuationLevel = 2
                }
            }
            val continueToLevel = it.getBoolean("Continue")
            if (continueToLevel) {
                currentLevel = it.getInt("ContinueLevel")
            }

            *///
        }
        getLogos(categoryId)
    }

    private fun continueLevel() {
        if (continuationLevel > 1) {
            val builder = AlertDialog.Builder(context!!)
            val dialogView = layoutInflater.inflate(R.layout.dialog_close_app, null)
            builder.setView(dialogView)
                .setPositiveButton(R.string.yes) { _, _ ->
                    arguments?.let {
                        categoryId = it.getInt(CategoriesActivity.CATEGORY)
                    }
                    val b = Bundle()
                    Log.d("Tää", "categoryId: $categoryId")
                    b.putInt(CategoriesActivity.CATEGORY, categoryId)
                    b.putBoolean("Continue", true)
                    b.putInt("ContinueLevel", continuationLevel)
                    CategoryFragment().arguments = b
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CategoryFragment()).commit()
                }
                .setNegativeButton(R.string.no) { _, _ ->
                }.show()
        }
    }


    private fun getLogos(categoryId: Int) {

        Log.d("Tää", "getLogos() $currentLevel")
        Log.d("Tää", "categoryId: $categoryId")

        when (categoryId) {
            1 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().clothesLogosLevelOne, LogoNames().clothesNamesLevelOne)
                    2 -> setLogos(Logos().clothesLogosLevelTwo, LogoNames().clothesNamesLevelTwo)
                    3 -> setLogos(Logos().clothesLogosLevelThree, LogoNames().clothesNamesLevelThree)
                    4 -> setLogos(Logos().clothesLogosLevelFour, LogoNames().clothesNamesLevelFour)
                    5 -> setLogos(Logos().clothesLogosLevelFive, LogoNames().clothesNamesLevelFive)
                    6 -> setLogos(Logos().clothesLogosLevelSix, LogoNames().clothesNamesLevelSix)
                    7 -> setLogos(Logos().clothesLogosLevelSeven, LogoNames().clothesNamesLevelSeven)
                    8 -> setLogos(Logos().clothesLogosLevelEight, LogoNames().clothesNamesLevelEight)
                    9 -> setLogos(Logos().clothesLogosLevelNine, LogoNames().clothesNamesLevelNine)
                    10 -> setLogos(Logos().clothesLogosLevelTen, LogoNames().clothesNamesLevelTen)
                }
            }
            2 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().drinkLogosLevelOne, LogoNames().drinkNamesLevelOne)
                    2 -> setLogos(Logos().drinkLogosLevelTwo, LogoNames().drinkNamesLevelTwo)
                    3 -> setLogos(Logos().drinkLogosLevelThree, LogoNames().drinkNamesLevelThree)
                    4 -> setLogos(Logos().drinkLogosLevelFour, LogoNames().drinkNamesLevelFour)
                    5 -> setLogos(Logos().drinkLogosLevelFive, LogoNames().drinkNamesLevelFive)
                    6 -> setLogos(Logos().drinkLogosLevelSix, LogoNames().drinkNamesLevelSix)
                    7 -> setLogos(Logos().drinkLogosLevelSeven, LogoNames().drinkNamesLevelSeven)
                    8 -> setLogos(Logos().drinkLogosLevelEight, LogoNames().drinkNamesLevelEight)
                    //9 -> setLogos(Logos().drinkLogosLevelNine, LogoNames().drinkNamesLevelNine)
                    //10 -> setLogos(Logos().drinkLogosLevelTen, LogoNames().drinkNamesLevelTen)
                }
            }
            3 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().entertainmentLogosLevelOne, LogoNames().entertainmentNamesLevelOne)
                    2 -> setLogos(Logos().entertainmentLogosLevelTwo, LogoNames().entertainmentNamesLevelTwo)
                    3 -> setLogos(Logos().entertainmentLogosLevelThree, LogoNames().entertainmentNamesLevelThree)
                    4 -> setLogos(Logos().entertainmentLogosLevelFour, LogoNames().entertainmentNamesLevelFour)
                    5 -> setLogos(Logos().entertainmentLogosLevelFive, LogoNames().entertainmentNamesLevelFive)
                    6 -> setLogos(Logos().entertainmentLogosLevelSix, LogoNames().entertainmentNamesLevelSix)
                    //7 -> setLogos(Logos().entertainmentLogosLevelSeven, LogoNames().entertainmentNamesLevelSeven)
                    //8 -> setLogos(Logos().entertainmentLogosLevelEight, LogoNames().entertainmentNamesLevelEight)
                    //9 -> setLogos(Logos().entertainmentLogosLevelNine, LogoNames().entertainmentNamesLevelNine)
                    //10 -> setLogos(Logos().entertainmentLogosLevelTen, LogoNames().entertainmentNamesLevelTen)

                }
            }
            4 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().foodLogosLevelOne, LogoNames().foodNamesLevelOne)
                    2 -> setLogos(Logos().foodLogosLevelTwo, LogoNames().foodNamesLevelTwo)
                    3 -> setLogos(Logos().foodLogosLevelThree, LogoNames().foodNamesLevelThree)
                    4 -> setLogos(Logos().foodLogosLevelFour, LogoNames().foodNamesLevelFour)
                    5 -> setLogos(Logos().foodLogosLevelFive, LogoNames().foodNamesLevelFive)
                    6 -> setLogos(Logos().foodLogosLevelSix, LogoNames().foodNamesLevelSix)
                    7 -> setLogos(Logos().foodLogosLevelSeven, LogoNames().foodNamesLevelSeven)
                    8 -> setLogos(Logos().foodLogosLevelEight, LogoNames().foodNamesLevelEight)
                    9 -> setLogos(Logos().foodLogosLevelNine, LogoNames().foodNamesLevelNine)
                    10 -> setLogos(Logos().foodLogosLevelTen, LogoNames().foodNamesLevelTen)
                }
            }
            5 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().gameLogosLevelOne, LogoNames().gameNamesLevelOne)
                    2 -> setLogos(Logos().gameLogosLevelTwo, LogoNames().gameNamesLevelTwo)
                    3 -> setLogos(Logos().gameLogosLevelThree, LogoNames().gameNamesLevelThree)
                    4 -> setLogos(Logos().gameLogosLevelFour, LogoNames().gameNamesLevelFour)
                    5 -> setLogos(Logos().gameLogosLevelFive, LogoNames().gameNamesLevelFive)
                    6 -> setLogos(Logos().gameLogosLevelSix, LogoNames().gameNamesLevelSix)
                    7 -> setLogos(Logos().gameLogosLevelSeven, LogoNames().gameNamesLevelSeven)
                    8 -> setLogos(Logos().gameLogosLevelEight, LogoNames().gameNamesLevelEight)
                    9 -> setLogos(Logos().gameLogosLevelNine, LogoNames().gameNamesLevelNine)
                    //10 -> setLogos(Logos().gameLogosLevelTen, LogoNames().gameNamesLevelTen)
                }
            }

            6 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().mediaLogosLevelOne, LogoNames().mediaNamesLevelOne)
                    2 -> setLogos(Logos().mediaLogosLevelTwo, LogoNames().mediaNamesLevelTwo)
                    3 -> setLogos(Logos().mediaLogosLevelThree, LogoNames().mediaNamesLevelThree)
                    4 -> setLogos(Logos().mediaLogosLevelFour, LogoNames().mediaNamesLevelFour)
                    5 -> setLogos(Logos().mediaLogosLevelFive, LogoNames().mediaNamesLevelFive)
                    6 -> setLogos(Logos().mediaLogosLevelSix, LogoNames().mediaNamesLevelSix)
                    //7 -> setLogos(Logos().mediaLogosLevelSeven, LogoNames().mediaNamesLevelSeven)
                    //8 -> setLogos(Logos().mediaLogosLevelEight, LogoNames().mediaNamesLevelEight)
                    //9 -> setLogos(Logos().mediaLogosLevelNine, LogoNames().mediaNamesLevelNine)
                    //10 -> setLogos(Logos().mediaLogosLevelTen, LogoNames().mediaNamesLevelTen)
                }
            }
            7 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().musicLogosLevelOne, LogoNames().musicNamesLevelOne)
                    2 -> setLogos(Logos().musicLogosLevelTwo, LogoNames().musicNamesLevelTwo)
                    3 -> setLogos(Logos().musicLogosLevelThree, LogoNames().musicNamesLevelThree)
                    4 -> setLogos(Logos().musicLogosLevelFour, LogoNames().musicNamesLevelFour)
                    5 -> setLogos(Logos().musicLogosLevelFive, LogoNames().musicNamesLevelFive)
                    6 -> setLogos(Logos().musicLogosLevelSix, LogoNames().musicNamesLevelSix)
                    7 -> setLogos(Logos().musicLogosLevelSeven, LogoNames().musicNamesLevelSeven)
                    8 -> setLogos(Logos().musicLogosLevelEight, LogoNames().musicNamesLevelEight)
                    //9 -> setLogos(Logos().musicLogosLevelNine, LogoNames().musicNamesLevelNine)
                    //10 -> setLogos(Logos().musicLogosLevelTen, LogoNames().musicNamesLevelTen)
                }
            }
            8 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().sportLogosLevelOne, LogoNames().sportNamesLevelOne)
                    2 -> setLogos(Logos().sportLogosLevelTwo, LogoNames().sportNamesLevelTwo)
                    3 -> setLogos(Logos().sportLogosLevelThree, LogoNames().sportNamesLevelThree)
                    4 -> setLogos(Logos().sportLogosLevelFour, LogoNames().sportNamesLevelFour)
                    5 -> setLogos(Logos().sportLogosLevelFive, LogoNames().sportNamesLevelFive)
                    6 -> setLogos(Logos().sportLogosLevelSix, LogoNames().sportNamesLevelSix)
                    7 -> setLogos(Logos().sportLogosLevelSeven, LogoNames().sportNamesLevelSeven)
                    8 -> setLogos(Logos().sportLogosLevelEight, LogoNames().sportNamesLevelEight)
                    //9 -> setLogos(Logos().sportLogosLevelNine, LogoNames().sportNamesLevelNine)
                    //10 -> setLogos(Logos().sportLogosLevelTen, LogoNames().sportNamesLevelTen)
                }
            }
            9 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().technologyLogosLevelOne, LogoNames().technologyNamesLevelOne)
                    2 -> setLogos(Logos().technologyLogosLevelTwo, LogoNames().technologyNamesLevelTwo)
                    3 -> setLogos(Logos().technologyLogosLevelThree, LogoNames().technologyNamesLevelThree)
                    4 -> setLogos(Logos().technologyLogosLevelFour, LogoNames().technologyNamesLevelFour)
                    5 -> setLogos(Logos().technologyLogosLevelFive, LogoNames().technologyNamesLevelFive)
                    6 -> setLogos(Logos().technologyLogosLevelSix, LogoNames().technologyNamesLevelSix)
                    7 -> setLogos(Logos().technologyLogosLevelSeven, LogoNames().technologyNamesLevelSeven)
                    8 -> setLogos(Logos().technologyLogosLevelEight, LogoNames().technologyNamesLevelEight)
                    9 -> setLogos(Logos().technologyLogosLevelNine, LogoNames().technologyNamesLevelNine)
                    10 -> setLogos(Logos().technologyLogosLevelTen, LogoNames().technologyNamesLevelTen)
                }
            }
            10 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().transportationLogosLevelOne, LogoNames().transportationNamesLevelOne)
                    2 -> setLogos(Logos().transportationLogosLevelTwo, LogoNames().transportationNamesLevelTwo)
                    3 -> setLogos(Logos().transportationLogosLevelThree, LogoNames().transportationNamesLevelThree)
                    4 -> setLogos(Logos().transportationLogosLevelFour, LogoNames().transportationNamesLevelFour)
                    5 -> setLogos(Logos().transportationLogosLevelFive, LogoNames().transportationNamesLevelFive)
                    6 -> setLogos(Logos().transportationLogosLevelSix, LogoNames().transportationNamesLevelSix)
                    7 -> setLogos(Logos().transportationLogosLevelSeven, LogoNames().transportationNamesLevelSeven)
                    //8 -> setLogos(Logos().transportationLogosLevelEight, LogoNames().transportationNamesLevelEight)
                    //9 -> setLogos(Logos().transportationLogosLevelNine, LogoNames().transportationNamesLevelNine)
                    //10 -> setLogos(Logos().transportationLogosLevelTen, LogoNames().transportationNamesLevelTen)
                }
            }
            11 -> {
                when (currentLevel) {
                    1 -> setLogos(Logos().vehicleLogosLevelOne, LogoNames().vehicleNamesLevelOne)
                    2 -> setLogos(Logos().vehicleLogosLevelTwo, LogoNames().vehicleNamesLevelTwo)
                    3 -> setLogos(Logos().vehicleLogosLevelThree, LogoNames().vehicleNamesLevelThree)
                    4 -> setLogos(Logos().vehicleLogosLevelFour, LogoNames().vehicleNamesLevelFour)
                    5 -> setLogos(Logos().vehicleLogosLevelFive, LogoNames().vehicleNamesLevelFive)
                    6 -> setLogos(Logos().vehicleLogosLevelSix, LogoNames().vehicleNamesLevelSix)
                    7 -> setLogos(Logos().vehicleLogosLevelSeven, LogoNames().vehicleNamesLevelSeven)
                    8 -> setLogos(Logos().vehicleLogosLevelEight, LogoNames().vehicleNamesLevelEight)
                    9 -> setLogos(Logos().vehicleLogosLevelNine, LogoNames().vehicleNamesLevelNine)
                    //10 -> setLogos(Logos().vehicleLogosLevelTen, LogoNames().vehicleNamesLevelTen)
                }
            }

        }
    }



    /*
    private fun getLogos(category: String) {

        when (category) {
            CategoriesActivity.BANDS -> {
                categoryId = 1
                when (currentLevel) {
                    1 -> setLogos(Logos().bandLogosLevelOne, LogoNames().bandNamesLevelOne)
                    2 -> setLogos(Logos().bandLogosLevelTwo, LogoNames().bandNamesLevelTwo)
                    //3 -> setLogos(Logos().bandLogosLevelThree, LogoNames().bandNamesLevelThree)
                    //4 -> setLogos(Logos().bandLogosLevelFour, LogoNames().bandNamesLevelFour)
                    //5 -> setLogos(Logos().bandLogosLevelFive, LogoNames().bandNamesLevelFive)
                }
            }
            CategoriesActivity.CARS -> {
                categoryId = 2
                when (currentLevel) {
                    1 -> setLogos(Logos().carLogosLevelOne, LogoNames().carNamesLevelOne)
                    2 -> setLogos(Logos().carLogosLevelTwo, LogoNames().carNamesLevelTwo)
                    //3 -> setLogos(Logos().carLogosLevelThree, LogoNames().carNamesLevelThree)
                    //4 -> setLogos(Logos().carLogosLevelFour, LogoNames().carNamesLevelFour)
                    //5 -> setLogos(Logos().carLogosLevelFive, LogoNames().carNamesLevelFive)
                }
            }
            CategoriesActivity.DRINKS -> {
                categoryId = 3
                when (currentLevel) {
                    1 -> setLogos(Logos().drinkLogosLevelOne, LogoNames().drinkNamesLevelOne)
                    2 -> setLogos(Logos().drinkLogosLevelTwo, LogoNames().drinkNamesLevelTwo)
                    //3 -> setLogos(Logos().drinkLogosLevelThree, LogoNames().drinkNamesLevelThree)
                    //4 -> setLogos(Logos().drinkLogosLevelFour, LogoNames().drinkNamesLevelFour)
                    //5 -> setLogos(Logos().drinkLogosLevelFive, LogoNames().drinkNamesLevelFive)
                }
            }
            CategoriesActivity.FOODS -> {
                categoryId = 4
                when (currentLevel) {
                    1 -> setLogos(Logos().foodLogosLevelOne, LogoNames().foodNamesLevelOne)
                    2 -> setLogos(Logos().foodLogosLevelTwo, LogoNames().foodNamesLevelTwo)
                    //3 -> setLogos(Logos().foodLogosLevelThree, LogoNames().foodNamesLevelThree)
                    //4 -> setLogos(Logos().foodLogosLevelFour, LogoNames().foodNamesLevelFour)
                    //5 -> setLogos(Logos().foodLogosLevelFive, LogoNames().foodNamesLevelFive)
                }
            }
            CategoriesActivity.SPORT_TEAMS -> {
                categoryId = 5
                when (currentLevel) {
                    1 -> setLogos(Logos().sportTeamLogosLevelOne, LogoNames().sportTeamNamesLevelOne)
                    2 -> setLogos(Logos().sportTeamLogosLevelTwo, LogoNames().sportTeamNamesLevelTwo)
                    3 -> setLogos(Logos().sportTeamLogosLevelThree, LogoNames().sportTeamNamesLevelThree)
                    4 -> setLogos(Logos().sportTeamLogosLevelFour, LogoNames().sportTeamNamesLevelFour)
                    //5 -> setLogos(Logos().sportTeamLogosLevelFive, LogoNames().sportTeamNamesLevelFive)
                }
            }
            CategoriesActivity.VIDEO_GAMES -> {
                categoryId = 6
                when (currentLevel) {
                    1 -> setLogos(Logos().videoGameLogosLevelOne, LogoNames().videoGameNamesLevelOne)
                    2 -> setLogos(Logos().videoGameLogosLevelTwo, LogoNames().videoGameNamesLevelTwo)
                    3 -> setLogos(Logos().videoGameLogosLevelThree, LogoNames().videoGameNamesLevelThree)
                    4 -> setLogos(Logos().videoGameLogosLevelFour, LogoNames().videoGameNamesLevelFour)
                    5 -> setLogos(Logos().videoGameLogosLevelFive, LogoNames().videoGameNamesLevelFive)
                }
            }
        }
    }
    */

    private fun setLogos(logos: IntArray, names: ArrayList<String>) {

        for (i in list.indices) {
            list[i].setImageResource(logos[i])
            list[i].tag = names[i]
        }

    }


    private fun createObserver() {
        db.quizDao().getLevels(categoryId).observe(this , Observer { it ->
            if (it != null){
                level.text = getString(R.string.level, it.current)
            }

        })
    }



}