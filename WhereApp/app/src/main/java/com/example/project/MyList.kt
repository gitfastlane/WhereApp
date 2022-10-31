package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

class MyList : AppCompatActivity() {
    var userID:String? = null

    var adapter:MemberAdapter? = null
    var memberList:ListView? = null

    var reHomeBtn:ImageButton? = null
    var goChildAddBtn:Button? = null
    var logoutBtn:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        userID = intent.getStringExtra("userID")
        var msg = intent.getStringExtra("msg")
        memberList = findViewById(R.id.myParentList)
        reHomeBtn = findViewById(R.id.reHomeFromMyParent)
        goChildAddBtn = findViewById(R.id.goChildAddBtn)
        logoutBtn = findViewById(R.id.logoutChildBtn)

        adapter = MemberAdapter(this,userID!!,msg!!)
        memberList!!.adapter = adapter

        reHomeBtn!!.setOnClickListener {
            var intent = Intent(this,Home::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
        }

        goChildAddBtn!!.setOnClickListener {
            var intent = Intent(this,AddChild::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
        }

        logoutBtn!!.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

}