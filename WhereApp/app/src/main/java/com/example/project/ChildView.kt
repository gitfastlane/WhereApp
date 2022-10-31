package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ChildView(context: Context):LinearLayout(context) {
    var idTxt:TextView
    var nameTxt:TextView
    var parentOkBtn:Button

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.child_view, this, true)

        idTxt = findViewById(R.id.childViewIdTxt)
        nameTxt = findViewById(R.id.childViewNameTxt)
        parentOkBtn = findViewById(R.id.parentOkBtn)
    }
    fun setting(mem:Child){
        idTxt.text = mem.c_id
        nameTxt.text = mem.c_name
    }
}