package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MemberView(context: Context?) :LinearLayout(context) {
    var userName:TextView
    var userId:TextView
    var deleteBtn:Button
    init {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.member_view, this, true)

        userName = findViewById(R.id.memberViewNameTxt)
        userId = findViewById(R.id.memberViewIdTxt)
        deleteBtn = findViewById(R.id.deleteBtn)
    }

    fun setting(mem:Child){
        userName.setText(mem.c_name)
        userId.setText(mem.c_id)
    }
}