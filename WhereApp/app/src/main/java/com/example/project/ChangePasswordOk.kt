package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class ChangePasswordOk : AppCompatActivity() {
    var service:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chang_password_ok)

        service = intent.getStringExtra("service")

    }
    fun LoginBoxGo(v:View){
        var intent = Intent(this,LoginBox::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }
}