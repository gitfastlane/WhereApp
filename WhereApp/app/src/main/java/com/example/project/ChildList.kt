package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

class ChildList : AppCompatActivity() {
    var userID:String? = null
    var adapter:ChildAdapter? = null
    var childList:ListView? = null
    var reHomeFromChild: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_list)

        userID = intent.getStringExtra("userID")
        var msg = intent.getStringExtra("msg")
        var childID = intent.getStringExtra("childID")

        reHomeFromChild = findViewById(R.id.reHomeFromList)
        childList = findViewById(R.id.parentList)
        adapter = ChildAdapter(this,userID!!,msg!!)
        childList!!.adapter = adapter

        reHomeFromChild!!.setOnClickListener {
            var intent = Intent(this,Home::class.java)
            intent.putExtra("userID",userID)
            intent.putExtra("childID",childID)
            intent.putExtra("flag","true")
            startActivity(intent)
        }
    }
}