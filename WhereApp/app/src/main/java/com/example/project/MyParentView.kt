package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

class MyParentView(context: Context?) :LinearLayout(context) {
    var myParentNameTxt:TextView
    var myParentIdTxt:TextView
    init {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.my_parent_view, this, true)

        myParentNameTxt= findViewById(R.id.myParentViewNameTxt)
        myParentIdTxt = findViewById(R.id.myParentViewIdTxt)
    }

    fun setting(mem:Parent){
        myParentNameTxt.setText(mem.p_name)
        myParentIdTxt.setText(mem.p_id)
    }
}