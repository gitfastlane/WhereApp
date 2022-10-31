package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

class MyParentList : AppCompatActivity() {
    var userID:String? = null
    var adapter:MyParentAdapter? = null
    var myParentList: ListView? = null
    var reHomeFromMyParent: ImageButton? = null
    var logoutChildBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_parent_list)

        userID = intent.getStringExtra("userID")
        var msg = intent.getStringExtra("msg")
        reHomeFromMyParent = findViewById(R.id.reHomeFromMyParent)
        logoutChildBtn = findViewById(R.id.logoutChildBtn)
        myParentList = findViewById(R.id.myParentList)
        adapter = MyParentAdapter(this,userID!!,msg!!)
        myParentList!!.adapter = adapter

        reHomeFromMyParent!!.setOnClickListener {
            var intent = Intent(this,HomeChild::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
        }

        logoutChildBtn!!.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}