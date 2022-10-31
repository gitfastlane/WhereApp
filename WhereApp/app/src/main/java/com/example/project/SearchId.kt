package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.Socket

class SearchId : AppCompatActivity(),MyExec {
    private var editSearchIdName: EditText?= null
    private  var editSearchIdPhone: EditText?=null

    private var btnSearchId: Button?= null

    var service:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_id)

        editSearchIdName = findViewById(R.id.editSearchIdName)
        editSearchIdPhone = findViewById(R.id.editSearchIdPhone)

        btnSearchId = findViewById(R.id.btnSearchId)
        if(intent.getStringExtra("service")!= null){
            service = intent.getStringExtra("service")
        }

        //접속 서버 dos 가져오기
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        btnSearchId!!.setOnClickListener {

            if(editSearchIdName!!.text.toString().isEmpty()){
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_LONG).show()
            }else if(editSearchIdPhone!!.text.toString().isEmpty()){
                Toast.makeText(this,"전화번호를 입력하세요", Toast.LENGTH_LONG).show()
            }
            else {
                Log.d("joinBox 여","회원가입 박스")
                if (dos != null) {
                    object : Thread() {
                        override fun run() {
                            dos!!.writeUTF("$service,SearchId,,,${editSearchIdPhone!!.text},${editSearchIdName!!.text}")
                            // dos!!.writeUTF("$service,Login,"+loginId!!.text.toString()+","+loginPw!!.text.toString()+","+",")
                        }
                    }.start()

                }
                false
            }

        }
    }
    fun mainGo(v: View){
        var intent = Intent(this,LoginBox::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }

    override fun execute(msg: String) {
        if (msg.equals("false")) {
            Toast.makeText(this@SearchId, "아이디 없음", Toast.LENGTH_LONG).show()
        } else {
            var intent = Intent(this@SearchId, SearchIdOk::class.java)
            intent.putExtra("service",service)
            intent.putExtra("id", msg)
            startActivity(intent)
        }
    }
}