package com.example.project

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//서버 생성
var rcTh = ReceiverThread()
var mapPinAR:MapPinAR = MapPinAR()
var mapPin:MapPinC = MapPinC()

class MainActivity : AppCompatActivity() {

    var service:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()

        //서버 접속
        if(rcTh.dos==null){
            rcTh.start()
        }

        //디폴트는 parent
        service = "parent"
        findViewById<RadioButton>(R.id.ma_parent).background = ContextCompat.getDrawable(this, R.drawable.select_button_left_click)

        findViewById<RadioGroup>(R.id.ma_radio_group)!!.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.ma_parent -> {
                    service = "parent"
                    findViewById<RadioButton>(R.id.ma_parent).background = ContextCompat.getDrawable(this, R.drawable.select_button_left_click)
                    findViewById<RadioButton>(R.id.ma_child).background = ContextCompat.getDrawable(this, R.drawable.select_button_right)
                }
                R.id.ma_child -> {
                    service = "child"
                    findViewById<RadioButton>(R.id.ma_parent).background = ContextCompat.getDrawable(this, R.drawable.select_button_left)
                    findViewById<RadioButton>(R.id.ma_child).background = ContextCompat.getDrawable(this, R.drawable.select_button_right_click)
                }
            }
        }
    }
    fun btnInputBoxGo(v:View){
        if(service!=null){
            var intent = Intent(this,InputBox::class.java)
            intent.putExtra("service",service)
            startActivity(intent)
        }
    }
    fun btnLoginBoxGo(v:View){
        if(service!=null){
            var intent = Intent(this,LoginBox::class.java)
            intent.putExtra("service",service)
            startActivity(intent)
        }
    }
    //권한 할당 함수
    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1234
        )
    }
}