package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.Socket

class SearchPassword : AppCompatActivity(),MyExec {
    var editSearchPwId:TextView ?= null
    var editSearchPwPhone:TextView ?= null

    var btnSearchPw: Button?= null

    var searchId:String ?= null
    var service:String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_password)

        editSearchPwId = findViewById(R.id.editSearchPwId)
        editSearchPwPhone = findViewById(R.id.editSearchPwPhone)

        btnSearchPw = findViewById(R.id.btnSearchPw)
        service = intent.getStringExtra("service")

        //접속 서버 dos 가져오기
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        btnSearchPw!!.setOnClickListener {
            if(editSearchPwId!!.text.toString().isEmpty()){
                Toast.makeText(this,"아이디를 입력하세요", Toast.LENGTH_LONG).show()
            }
            else if(editSearchPwPhone!!.text.toString().isEmpty()){
                Toast.makeText(this,"전화번호를 입력하세요", Toast.LENGTH_LONG).show()
            }else {
                Log.d("joinBox 여","회원가입 박스")
                searchId = editSearchPwId!!.text.toString()

                if (dos != null) {
                    object : Thread() {
                        override fun run() {
                            dos!!.writeUTF("$service,SearchPw,${editSearchPwId!!.text},,${editSearchPwPhone!!.text},")
                        }
                    }.start()
                }
                false
            }
        }
    }
    fun LoginGo(v: View){
        var intent = Intent(this,LoginBox::class.java)
        intent.putExtra("service",service)
        intent.putExtra("id",searchId)

        startActivity(intent)
    }

    override fun execute(msg: String) {
        if(msg.equals("false")) {
            Toast.makeText(this@SearchPassword, "비밀번호 없음", Toast.LENGTH_LONG).show()
        }else{
            var intent = Intent(this@SearchPassword,ChangePassword::class.java)
            intent.putExtra("service",service)
            //  intent.putExtra("id",editSearchPwId!!.text)
            intent.putExtra("id",searchId)
            startActivity(intent)
        }
    }


}