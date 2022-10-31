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

class LoginBox : AppCompatActivity(), MyExec{
    private var lb_input_id: EditText?= null
    private var lb_input_pw: EditText?= null

    var lb_loginBtn:Button ?= null

    var service:String ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_box)

        lb_input_id = findViewById(R.id.lb_input_id)
        lb_input_pw = findViewById(R.id.lb_input_pw)
        lb_loginBtn = findViewById(R.id.lb_loginBtn)

        if(intent.getStringExtra("service") != null){
            service = intent.getStringExtra("service")
        }
        if(intent.getStringExtra("id")!=null){
            lb_input_id!!.setText(intent.getStringExtra("id"))
        }

        //접속 서버 dos 가져오기
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        lb_loginBtn!!.setOnClickListener {
            Log.d("btnLogin 여","로그인 버튼")
            if(lb_input_id!!.text.toString().isEmpty()){
                Toast.makeText(this,"아이디를 입력하세요", Toast.LENGTH_LONG).show()
            }else if(lb_input_pw!!.text.toString().isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
            }
            else if(dos!=null){
                object:Thread(){
                    override fun run() {
                        dos!!.writeUTF("$service,Login,"+lb_input_id!!.text.toString()+","+lb_input_pw!!.text.toString()+","+",")
                    }

                }.start()
            }
            false
        }
    }
    fun MainGo(v:View){
        var intent = Intent(this,MainActivity::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }
    fun inputBoxGo(v:View){
        var intent = Intent(this,InputBox::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }
    fun searchIdBoxGo(v:View){
        var intent = Intent(this,SearchId::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }
    fun saarchPwBoxGo(v: View){
        var intent = Intent(this,SearchPassword::class.java)
        intent.putExtra("service",service)
        startActivity(intent)

    }

    override fun execute(msg: String) {
        Log.d("로그인 여부 여", "$msg")
        if (msg == "true") {
            if(service=="parent"){
                var intent = Intent(this, Home::class.java)
                intent.putExtra("userID",lb_input_id!!.text.toString())
                startActivity(intent)
            }else if(service=="child"){
                var intent = Intent(this, HomeChild::class.java)
                intent.putExtra("userID",lb_input_id!!.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this@LoginBox, "서비스 선택 내역이 없습니다.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this@LoginBox, "로그인 실패", Toast.LENGTH_SHORT).show();
        }
    }


}