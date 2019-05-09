package com.example.leopa.logoquiz.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.transition.TransitionInflater
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.LinearLayout
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.view.inputmethod.EditorInfo
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.R
import com.example.leopa.logoquiz.StringUtils
import org.jetbrains.anko.doAsync
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import java.util.*


class DetailFragment: Fragment() {

    private lateinit var letterList: MutableList<EditText>

    private lateinit var logoNamePart1: LinearLayout
    private lateinit var logoNamePart2: LinearLayout
    private lateinit var logoNamePart3: LinearLayout
    private lateinit var logoNamePart4: LinearLayout
    private lateinit var logoNamePart5: LinearLayout

    private lateinit var checkButton: Button
    private lateinit var vibrator: Vibrator

    private lateinit var detailLayout: LinearLayout

    private lateinit var detailImage: ImageView
    private lateinit var logoNameTv: TextView
    private lateinit var logoName: String
    private lateinit var originalLogoName: String
    private lateinit var accentedLogoName: String

    private var logoPart1: String? = null
    private var logoPart2: String? = null
    private var logoPart3: String? = null
    private var logoPart4: String? = null
    private var logoPart5: String? = null

    private lateinit var congratulations: ImageView
    private lateinit var db: DataBase
    private var openLevel = false

    private var categoryId = 1

    private var guessed = false

    private lateinit var correct: MediaPlayer
    private lateinit var incorrect: MediaPlayer
    private lateinit var levelUnlocked: MediaPlayer
    private lateinit var almost: MediaPlayer
    private lateinit var tap: MediaPlayer

    private var vibrationOff: Boolean = false
    private var soundsOff: Boolean = false

    companion object {
        const val VIBRATION_TIME_SHORT: Long = 100
        const val VIBRATION_TIME_REGULAR: Long = 700
        const val VIBRATION_TIME_LONG: Long = 1000
        const val LOGO_GUESSED: Long = 2000
        const val LEVEL_UNLOCKED: Long = 3000
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)
        db = DataBase.get(context!!)

        correct = MediaPlayer.create(context!!, R.raw.correct)
        incorrect = MediaPlayer.create(context!!, R.raw.incorrect)
        levelUnlocked = MediaPlayer.create(context!!, R.raw.levelunlocked)
        almost = MediaPlayer.create(context!!, R.raw.almost)
        tap = MediaPlayer.create(context!!, R.raw.tap)

        vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        //

        settingsStatus()

        //

        detailImage = rootView.findViewById(R.id.detail_image)
        logoNameTv = rootView.findViewById(R.id.logo_name)
        detailLayout = rootView.findViewById(R.id.detail_layout)
        detailLayout.setOnTouchListener { v, event ->
            val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

            imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)

            false
        }

        getLogoDetails()

        congratulations = rootView.findViewById(R.id.congratulations)


        logoNamePart1 = rootView.findViewById(R.id.logo_name_part_1)
        logoNamePart2 = rootView.findViewById(R.id.logo_name_part_2)
        logoNamePart3 = rootView.findViewById(R.id.logo_name_part_3)
        logoNamePart4 = rootView.findViewById(R.id.logo_name_part_4)
        logoNamePart5 = rootView.findViewById(R.id.logo_name_part_5)

        letterList = mutableListOf()


        setLogoName()

        checkButton = rootView.findViewById(R.id.check_button)
        setCheckButtonClickListener()


        return rootView
    }


    private fun settingsStatus() {
        doAsync {
            vibrationOff = db.quizDao().getVibrationStatus()
            soundsOff = db.quizDao().getSoundsStatus()
        }
    }

    private fun getLogoDetails() {
        arguments?.let {
            detailImage.transitionName = it.getString("Shared element")!!
            val byteArray = it.getByteArray("Logo")!!
            val logo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            detailImage.setImageBitmap(logo)
            originalLogoName = it.getString("Name")!!

            logoName = originalLogoName
                .replace("C&A".toRegex(), "c and a")
                .replace("&".toRegex(), "and")
                .replace("The ".toRegex(), "")
                .replace("AFC ".toRegex(), "")
                .replace("PFC ".toRegex(), "")
                .replace("FC ".toRegex(), "")
                .replace(" FC".toRegex(), "")
                .replace(" F.C.".toRegex(), "")
                .replace(" S.K.".toRegex(), "")
                .replace("C.A. ".toRegex(), "")
                .replace(" C.F.".toRegex(), "")
                .replace("S.L. ".toRegex(), "")
                .replace("A.S. ".toRegex(), "")
                .replace("VfL ".toRegex(), "")
                .replace("S.S.C. ".toRegex(), "")
                .replace(" B.C.".toRegex(), "")
                .replace(" 09 e.V.".toRegex(), "")
                .replace(" e.V.".toRegex(), "")
                .replace(" FF".toRegex(), "")
                .replace(".tv".toRegex(), "")
                .replace(" A ".toRegex(), "")
                .replace(" S.A.".toRegex(), "")
                .replace("\\+".toRegex(), " plus")
                .replace("50".toRegex(), "fifty")
                .replace("64".toRegex(), "sixty-four")
                .replace("20th".toRegex(), "twentieth")
                .replace("21st".toRegex(), "twenty-first")
                .replace("1".toRegex(), "one")
                .replace("2".toRegex(), "two")
                .replace("3".toRegex(), "three")
                .replace("4".toRegex(), "four")
                .replace("5".toRegex(), "five")
                .replace("6".toRegex(), "six")
                .replace("7".toRegex(), "seven")
                .replace("8".toRegex(), "eight")
                .replace("9".toRegex(), "nine")
                .replace("-".toRegex(), " ").toLowerCase()

            //Log.d("Tää", logoName)
            //logoName = logoName.replace("&".toRegex(), "and").toLowerCase()

            accentedLogoName = logoName
            logoName = StringUtils.removeUmlauts(logoName)


            val re = Regex("[^A-Za-z0-9 ]")
            logoName = re.replace(logoName, "")


            categoryId = it.getInt("Category")

            doAsync {

                val guessed = 0 //get count of guessed logos from database
                /*
                if (guessed == 6 || guessed == 13 || guessed == 20 || guessed == 27) {
                    openLevel = true
                }
                */
            }

        }
    }

    private fun setCheckButtonClickListener() {
        checkButton.setOnClickListener {
            var guess = ""
            for (i in 0 until letterList.size) {
                guess += letterList[i].text
            }
            guess = guess.toLowerCase()
            val name = logoName.replace(" ".toRegex(), "")
            val unaccented = accentedLogoName.replace(" ".toRegex(), "")

            if (guess.length == name.length) {

                var correctLetters = 0
                var closeGuess = false

                val g = guess.toCharArray()
                val n = name.toCharArray()
                val u = unaccented.toCharArray()

                for (i in 0 until letterList.size) {
                    if (g[i] == n[i]) { correctLetters += 1 }
                    else if (g[i] == u[i]) { correctLetters += 1 }
                }
                correctLetters = name.length - correctLetters

                /*
                if (correctLetters == 1) { closeGuess = true }
                else if (correctLetters == 2) {
                    if (g.toSortedSet() == n.toSortedSet() || g.toSortedSet() == u.toSortedSet()) {
                        closeGuess = true
                    }
                }
                */

                when (correctLetters) {
                    1 -> closeGuess = true
                    2 -> if (g.toSortedSet() == n.toSortedSet() || g.toSortedSet() == u.toSortedSet()) {
                        closeGuess = true
                    }
                }



                if (guess == name || guess == unaccented) {
                    val vibration: Long?
                    val time: Long?
                    val message: String?
                    val toast: Int?
                    if (openLevel) {
                        if (!soundsOff) {
                            levelUnlocked.start()
                        }
                        //levelUnlocked.start()
                        time = LEVEL_UNLOCKED

                        vibration = if (!vibrationOff) {
                            VIBRATION_TIME_LONG
                        } else {
                            0.toLong()
                        }
                        //vibration = VIBRATION_TIME_LONG

                        message = "New level unlocked!"
                        toast = Toast.LENGTH_LONG
                        congratulations.setBackgroundResource(R.drawable.congratulations)
                        congratulations.post {
                            val frameAnimation = congratulations.background as AnimationDrawable
                            frameAnimation.start()
                        }

                    } else {
                        if (!soundsOff) {
                            correct.start()
                        }
                        //correct.start()
                        time = LOGO_GUESSED

                        vibration = if (!vibrationOff) {
                            VIBRATION_TIME_REGULAR
                        } else {
                            0.toLong()
                        }
                        //vibration = VIBRATION_TIME_REGULAR

                        message = "Correct!"
                        toast = Toast.LENGTH_SHORT
                    }


                    object : CountDownTimer(time, time) {
                        override fun onTick(millisUntilFinished: Long) {


                            checkButton.isEnabled = false

                            val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            lp.height = 0
                            lp.width = 0

                            logoNamePart1.layoutParams = lp
                            logoNamePart2.layoutParams = lp
                            logoNamePart3.layoutParams = lp
                            logoNamePart4.layoutParams = lp
                            logoNamePart5.layoutParams = lp

                            logoNameTv.text = originalLogoName

                            Toast.makeText(context!!, message, toast).show()
                            vibrator.vibrate(vibration)
                        }
                        override fun onFinish() {
                            val currentFragment = activity?.supportFragmentManager?.findFragmentByTag("Detail")
                            if (currentFragment != null && currentFragment.isVisible) {
                                if (currentFragment.isStateSaved) {
                                    guessed = true
                                }
                                else {
                                    fragmentManager!!.popBackStack("tag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                }
                            }
                        }
                    }.start()
                }
                else if (closeGuess) {
                    if (!soundsOff) {
                        almost.start()
                    }
                    //almost.start()
                    shakeImage()
                    Toast.makeText(context!!, "Almost correct", Toast.LENGTH_SHORT).show()
                    if (!vibrationOff) {
                        vibrator.vibrate(VIBRATION_TIME_SHORT)
                    }
                    //vibrator.vibrate(VIBRATION_TIME_SHORT)
                }
                else {
                    if (!soundsOff) {
                        incorrect.start()
                    }
                    //incorrect.start()
                    shakeImage()
                    Toast.makeText(context!!, "Wrong!", Toast.LENGTH_SHORT).show()

                    if (!vibrationOff) {
                        vibrator.vibrate(VIBRATION_TIME_SHORT)
                    }
                    //vibrator.vibrate(VIBRATION_TIME_SHORT)
                }
            }
            else {
                if (!soundsOff) {
                    incorrect.start()
                }
                shakeImage()
                Toast.makeText(context!!, "Wrong!", Toast.LENGTH_SHORT).show()
                if (!vibrationOff) {
                    vibrator.vibrate(VIBRATION_TIME_SHORT)
                }
            }

        }
    }

    override fun onResume() {
        if (guessed) { fragmentManager!!.popBackStack("tag", FragmentManager.POP_BACK_STACK_INCLUSIVE) }
        super.onResume()
    }

    private fun setLogoName() {
        if (logoName.contains(" ")) {

            logoPart1 = logoName.split(" ")[0]
            logoPart2 = logoName.split(" ")[1]

            setFirstPart()

            /*
            when {
                logoName.split(" ").size == 2 -> {
                    setSecondPart(2)
                }
                logoName.split(" ").size == 3 -> {
                    logoPart3 = logoName.split(" ")[2]
                    setSecondPart(3)
                    setThirdPart(3)
                }
                else -> {
                    logoPart3 = logoName.split(" ")[2]
                    logoPart4 = logoName.split(" ")[3]
                    setSecondPart(4)
                    setThirdPart(4)
                    setFourthPart()
                }
            }
            */
            when {
                logoName.split(" ").size == 2 -> {
                    setSecondPart(2)
                }
                logoName.split(" ").size == 3 -> {
                    logoPart3 = logoName.split(" ")[2]
                    setSecondPart(3)
                    setThirdPart(3)
                }
                logoName.split(" ").size == 4 -> {
                    logoPart3 = logoName.split(" ")[2]
                    logoPart4 = logoName.split(" ")[3]
                    setSecondPart(4)
                    setThirdPart(4)
                    setFourthPart(4)
                }
                else -> {
                    logoPart3 = logoName.split(" ")[2]
                    logoPart4 = logoName.split(" ")[3]
                    logoPart5 = logoName.split(" ")[4]
                    setSecondPart(5)
                    setThirdPart(5)
                    setFourthPart(5)
                    setFifthPart()
                }
            }


        }
        else {
            setOneWord()
        }
    }


    private fun setEditTextMargins(logoPart: String, lp: LinearLayout.LayoutParams) {
        when {
            logoPart.length > 13 -> {
                lp.setMargins(2, 0, 2, 0)
                lp.width = 70
            }
            logoPart.length > 10 -> {
                lp.setMargins(3, 0, 3, 0)
                lp.width = 75
            }
            logoPart.length > 9 -> {
                lp.setMargins(5, 0, 5, 0)
                lp.width = 85
            }
            logoPart.length > 8 -> {
                lp.setMargins(8, 0, 8, 0)
                lp.width = 90
            }
            else -> {
                lp.setMargins(10, 0, 10, 0)
                lp.width = 100
            }
        }
    }


    private fun setOneWord() {
        for (i in 0 until logoName.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            setEditTextMargins(logoName, lp)


            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(i, editText)
            logoNamePart1.addView(editText)

            when (i) {
                0 -> addFirstTextChangeListener(editText)
                logoName.length-1 -> addLastTextChangeListener(editText)
                else -> addMiddleTextChangeListener(editText)
            }
        }
    }

    private fun setFirstPart() {
        for (i in 0 until logoPart1!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart1!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(i, editText)
            logoNamePart1.addView(editText)

            when (i) {
                0 -> addFirstTextChangeListener(editText)
                else -> addMiddleTextChangeListener(editText)
            }
        }
    }

    private fun setSecondPart(part: Int) {

        for (i in 0 until logoPart2!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart2!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(letterList.size, editText)
            logoNamePart2.addView(editText)

            if (part == 2) {
                when (i) {
                    logoPart2!!.length-1 -> addLastTextChangeListener(editText)
                    else -> addMiddleTextChangeListener(editText)
                }
            }
            else {
                addMiddleTextChangeListener(editText)
            }
        }
    }

    private fun setThirdPart(part: Int) {

        for (i in 0 until logoPart3!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart3!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(letterList.size, editText)
            logoNamePart3.addView(editText)


            if (part == 3) {
                when (i) {
                    logoPart3!!.length-1 -> addLastTextChangeListener(editText)
                    else -> addMiddleTextChangeListener(editText)
                }
            }
            else {
                addMiddleTextChangeListener(editText)
            }
        }
    }

    /*
    private fun setFourthPart() {
        for (i in 0 until logoPart4!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart4!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(letterList.size, editText)
            logoNamePart4.addView(editText)

            when (i) {
                logoPart4!!.length-1 -> addLastTextChangeListener(editText)
                else -> addMiddleTextChangeListener(editText)
            }
        }
    }
    */

    private fun setFourthPart(part: Int) {
        for (i in 0 until logoPart4!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart4!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(letterList.size, editText)
            logoNamePart4.addView(editText)


            if (part == 4) {
                when (i) {
                    logoPart4!!.length-1 -> addLastTextChangeListener(editText)
                    else -> addMiddleTextChangeListener(editText)
                }
            }
            else {
                addMiddleTextChangeListener(editText)
            }
        }

    }

    private fun setFifthPart() {
        for (i in 0 until logoPart5!!.length) {
            val editText = EditText(context!!)
            editText.setSingleLine()
            editText.imeOptions = (EditorInfo.IME_ACTION_DONE)

            //editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //lp.setMargins(10, 0, 10, 0)
            //lp.width = 100
            setEditTextMargins(logoPart5!!, lp)

            editText.layoutParams = lp
            editText.setBackgroundResource(R.drawable.edit_text_boxes)
            editText.gravity = Gravity.CENTER

            letterList.add(letterList.size, editText)
            logoNamePart5.addView(editText)

            when (i) {
                logoPart5!!.length-1 -> addLastTextChangeListener(editText)
                else -> addMiddleTextChangeListener(editText)
            }
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementEnterTransition = TransitionInflater
            .from(context).inflateTransition(
                android.R.transition.move
            )
        super.onCreate(savedInstanceState)
    }

    private fun addFirstTextChangeListener(editText: EditText) {
        changeFocus(editText, true)
        setFocus(editText)
    }

    private fun addMiddleTextChangeListener(editText: EditText) {
        changeFocus(editText, true)
        setFocus(editText)

        var backspace = true

        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (editText.text.isEmpty() && !backspace) {
                    val next = editText.focusSearch(View.FOCUS_BACKWARD)
                    next?.requestFocus()
                    backspace = true
                }
                else {
                    backspace = false
                }
            }
            false
        }



    }

    private fun addLastTextChangeListener(editText: EditText) {
        changeFocus(editText, false)
        setFocus(editText)

        //var backspace = true

        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                if (editText.text.isEmpty() && !backspace) {
                    val next = editText.focusSearch(View.FOCUS_BACKWARD)
                    next?.requestFocus()
                    backspace = true
                }
                else {
                    backspace = false
                }
            }
            false
        }
    }

    private var backspace = true
    private var clicked = false

    private fun setFocus(editText: EditText) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            editText.isCursorVisible = false
            if (hasFocus && clicked) {
                editText.setText("")
                clicked = false
            }
            if (!soundsOff) {
                tap.start()
            }
        }
        editText.setOnTouchListener { v, event ->
            clicked = true
            false
        }
    }

    private fun changeFocus(editText: EditText, forward: Boolean) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (editText.length() > 0) {
                    if (editText.length() > 1) {
                        editText.text.delete(0,1)
                    }
                    if (forward) {
                        val next = editText.focusSearch(View.FOCUS_FORWARD)
                        next?.requestFocus()
                    }
                    else {
                        val layout = activity!!.findViewById(R.id.detail_layout) as LinearLayout
                        layout.requestFocus()
                        val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editText.windowToken, 0)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun shakeImage() {
        val shake: Animation = AnimationUtils.loadAnimation(activity!!.applicationContext,
            R.anim.shake
        )
        detailImage.startAnimation(shake)
    }

}