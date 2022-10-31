package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

class ChangePassword : AppCompatActivity(), MyExec {
    var editChangPw:EditText ?= null
    var editChangPw2:EditText ?= null

    var btnChangePw: Button?= null
    var inputId:String ?= null
    var service:String ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_passwrod)

        editChangPw = findViewById(R.id.editChangPw)
        editChangPw2 = findViewById(R.id.editChangPw2)
        btnChangePw = findViewById(R.id.btnChangePw)

        inputId = intent.getStringExtra("id")
        service = intent.getStringExtra("service")

        //접속 서버 dos 가져오기
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        btnChangePw!!.setOnClickListener {
            if(editChangPw!!.text.toString().isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
            }else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{5,15}$", editChangPw!!.text.toString())) {
                Toast.makeText(this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            } else if(editChangPw2!!.text.toString() != editChangPw!!.text.toString()){
                Toast.makeText(this,"비밀번호가 서로 다릅니다.", Toast.LENGTH_LONG).show()
            }

            else {
                Log.d("joinBox 여","회원가입 박스")

                if (dos != null) {
                    object : Thread() {
                        override fun run() {
                            dos!!.writeUTF("$service,ChangePw,$inputId,${editChangPw!!.text},,")
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
        if(msg=="false"){
            Toast.makeText(this@ChangePassword, "비밀번호 변경 실패", Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(this@ChangePassword, "비밀번호 변경 성공", Toast.LENGTH_LONG).show()
            var intent = Intent(this@ChangePassword,ChangePasswordOk::class.java)
            intent.putExtra("service",service)
            startActivity(intent)
        }
    }

}