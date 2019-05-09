package com.example.leopa.logoquiz.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.leopa.logoquiz.R

class ListAdapter(var cont: Context, var resource: Int, var items: List<Model>)
    :ArrayAdapter<Model>( cont , resource , items ){

    private val viewGroup: ViewGroup? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(cont)

        val view: View = layoutInflater.inflate(resource, viewGroup)
        val imageView: ImageView = view.findViewById(R.id.iconIv)
        val textView: TextView = view.findViewById(R.id.titleTv)
        val textView1: TextView = view.findViewById(R.id.descTv)

        val category: Model = items[position]


        imageView.setImageDrawable(cont.resources.getDrawable(category.photo))
        textView.text = category.title
        textView1.text = category.desc


        return view
    }

}