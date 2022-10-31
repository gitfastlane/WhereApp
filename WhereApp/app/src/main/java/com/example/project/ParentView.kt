package com.example.project

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ParentView(context: Context?) :LinearLayout(context) {
    var parentNameTxt:TextView
    var parentIdTxt:TextView
    var parentOkBtn:Button
    init {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.parent_view, this, true)

        parentNameTxt= findViewById(R.id.parentViewNameTxt)
        parentIdTxt = findViewById(R.id.parentViewIdTxt)
        parentOkBtn = findViewById(R.id.parentOkBtn)
    }

    fun setting(mem:Parent){
        Log.d("socket", "parentView setting : ${mem.p_id}")
        parentNameTxt.setText(mem.p_name)
        parentIdTxt.setText(mem.p_id)
    }
}