package com.example.leopa.logoquiz.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.leopa.logoquiz.R

class GuidelinesFragment: Fragment() {

    private lateinit var title: TextView

    companion object {
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_guidelines, container, false)

        title = rootView.findViewById(R.id.guidelineTitle)

        return rootView
    }
}