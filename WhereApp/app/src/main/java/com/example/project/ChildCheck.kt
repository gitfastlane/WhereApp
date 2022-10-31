package com.example.project


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChildCheck : AppCompatActivity() {

    var childNameTxt:TextView? = null
    var moreChildBtn:Button? = null
    var nextHomeBtn:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_check)

        childNameTxt = findViewById(R.id.childNameTxt)
        moreChildBtn = findViewById(R.id.moreChildBtn)
        nextHomeBtn = findViewById(R.id.nextHomeBtn)

        val userID = intent.getStringExtra("userID")
        val c_name = intent.getStringExtra("c_name")

        childNameTxt!!.text = c_name
        moreChildBtn!!.setOnClickListener {
            val addIntent = Intent(this,AddChild::class.java)
            addIntent.putExtra("userID",userID)
            startActivity(addIntent)
        }
        //확인 클릭 -> 메인화면으로 이동
        nextHomeBtn!!.setOnClickListener {
            val addIntent = Intent(this,Home::class.java)
            addIntent.putExtra("userID",userID)
            startActivity(addIntent)
        }

    }
}