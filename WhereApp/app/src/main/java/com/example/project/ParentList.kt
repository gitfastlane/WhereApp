package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

class ParentList : AppCompatActivity() {
    var userID:String? = null
    var adapter:ParentAdapter? = null
    var parentList: ListView? = null
    var reHomeFromList: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_list)

        userID = intent.getStringExtra("userID")
        var msg = intent.getStringExtra("msg")
        reHomeFromList = findViewById(R.id.reHomeFromList)
        parentList = findViewById(R.id.parentList)
        adapter = ParentAdapter(this,userID!!,msg!!)
        parentList!!.adapter = adapter

        reHomeFromList!!.setOnClickListener {
            var intent = Intent(this,HomeChild::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
        }
    }
}