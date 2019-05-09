package com.example.leopa.logoquiz.activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.leopa.logoquiz.R
import com.example.leopa.logoquiz.database.DataBase
import com.example.leopa.logoquiz.fragments.CategoryFragment

class CategoryActivity : AppCompatActivity() {

    private val categoryFragment = CategoryFragment()
    private lateinit var category: String
    private var categoryId = 0

    private lateinit var db: DataBase

    //var adapterViewPager: FragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        db = DataBase.get(this)

        val extras = intent.extras
        /*
        category =  extras?.getString(CategoriesActivity.CATEGORY)!!
        val b = Bundle()
        b.putString(CategoriesActivity.CATEGORY, category)
        categoryFragment.arguments = b
        setupFragment(categoryFragment, category)
        */

        categoryId =  extras?.getInt(CategoriesActivity.CATEGORY)!!
        val b = Bundle()
        b.putInt(CategoriesActivity.CATEGORY, categoryId)
        b.putBoolean("Progress", true)
        categoryFragment.arguments = b
        setupFragment(categoryFragment, "Nameless")



        /*

        val vpPager = findViewById<View>(R.id.vpPager) as ViewPager
        adapterViewPager = ViewPagerAdapter(supportFragmentManager, category)
        vpPager.adapter = adapterViewPager


        vpPager.addOnPageChangeListener(object : OnPageChangeListener {

            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {
                Toast.makeText(
                    this@CategoryActivity,
                    "Selected page position: $position", Toast.LENGTH_SHORT
                ).show()
            }

            // This method will be invoked when the current page is scrolled
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            override fun onPageScrollStateChanged(state: Int) {
                // Code goes here
            }
        })

        */



    }

    override fun onRestart() {
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onRestart()
    }

    private fun setupFragment(fragment: Fragment, name: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, name).commit()
    }


}