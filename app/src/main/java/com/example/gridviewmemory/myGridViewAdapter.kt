package com.example.gridviewmemory

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout

class MyGridViewAdapter internal constructor(context: Context) : BaseAdapter() {
    private val context: Context
    private var drawables: List<Int>? = null
    fun setDrawables(drawables: List<Int>?) {
        this.drawables = drawables
    }

    override fun getCount(): Int {
        return drawables!!.size
    }

    override fun getItem(position: Int): Any {
        return drawables!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.ic_android_black_24dp)
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        imageView.setLayoutParams(
            RelativeLayout.LayoutParams(270 , 200 )
        )
        return imageView
    }

    init {
        this.context = context
    }
}