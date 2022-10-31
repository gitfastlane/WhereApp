package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class SearchIdOk : AppCompatActivity() {
    var searchId:String ?= null
    var service:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_id_ok)

        if(intent.getStringExtra("service") != null){
            service = intent.getStringExtra("service")
        }

        if(intent.getStringExtra("id") != null){
            findViewById<TextView>(R.id.searchId).text = intent.getStringExtra("id")
            searchId = intent.getStringExtra("id")
        }

    }
    fun MainGo(v: View){
        var intent = Intent(this,LoginBox::class.java)
        intent.putExtra("id",searchId)
        intent.putExtra("service",service)
        startActivity(intent)
    }

    fun PasswordBoxGo(v:View){
        var intent = Intent(this,SearchPassword::class.java)
        intent.putExtra("id",searchId)
        intent.putExtra("service",service)
        startActivity(intent)
    }
}