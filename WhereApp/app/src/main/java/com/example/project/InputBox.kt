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
import java.util.regex.Pattern

class InputBox : AppCompatActivity(), MyExec {
    var ib_id: EditText?=null
    var ib_pw: EditText?= null
    var ib_checkpw: EditText?= null
    var ib_name: EditText?= null
    var ib_phone: EditText?=null

    var service:String ?=null

    var ib_checkIdBtn:Button ?= null
    var ib_registerBtn: Button?= null
    var idCheckB:Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_box)

        //입력창
        ib_id = findViewById(R.id.ib_id)
        ib_pw = findViewById(R.id.ib_pw)
        ib_checkpw = findViewById(R.id.ib_checkpw)
        ib_name = findViewById(R.id.ib_name)
        ib_phone = findViewById(R.id.ib_phone)

        //아이디 확인
        ib_checkIdBtn = findViewById(R.id.ib_checkIdBtn)
        //회원가입
        ib_registerBtn = findViewById(R.id.ib_registerBtn)


        if(intent.getStringExtra("service") != null){
            service = intent.getStringExtra("service")
        }

        //아이디 클릭시 체크확인  -->추후수정
        ib_id!!.setOnClickListener {
            idCheckB = false
        }

        //접속 서버 dos 가져오기
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        ib_registerBtn!!.setOnClickListener {
            //유효성 순서 아-비-비확-이름-전화
            //아이디
            if(ib_id!!.text.toString().isEmpty()){
                Toast.makeText(this,"아이디를 입력하세요", Toast.LENGTH_LONG).show()
            }else if(ib_id!!.text.toString().length < 5){
                Toast.makeText(this,"아이디는 5자리 이상으로 입력하세요", Toast.LENGTH_SHORT).show();
            }else if(!Pattern.matches("^[a-zA-Z0-9]*\$", ib_id!!.text.toString())) {
                Toast.makeText(this,"아이디의 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            }else if(!idCheckB){
                Toast.makeText(this,"아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            //비밀번호
            else if(ib_pw!!.text.toString().isEmpty()){
                Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
            }else if(ib_pw!!.text.toString().length < 5) {
                Toast.makeText(this, "비밀번호는 5자리 이상으로 입력하세요", Toast.LENGTH_SHORT).show();
            } else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{5,15}$", ib_pw!!.text.toString())) {
                Toast.makeText(this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            }

            //비밀번호 확인
            else if(ib_pw!!.text.toString() != (ib_checkpw!!.text.toString())){
                Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }

            //이름
            else if(ib_name!!.text.toString().isEmpty()){
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_LONG).show()
            }

            //전화번호
            else if(ib_phone!!.text.toString().isEmpty()){
                Toast.makeText(this,"전화번호를 입력하세요", Toast.LENGTH_LONG).show()
            }else if(ib_phone!!.text.toString().length < 10){
                Toast.makeText(this, "전화번호는 10자리 이상으로 입력하세요", Toast.LENGTH_SHORT).show();
            }else if(!Pattern.matches("^01([0|1|2|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})\$", ib_phone!!.text.toString())) {
                Toast.makeText(this,"전화번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            }

            else if (dos != null) {
                object : Thread() {
                    override fun run() {
                        dos!!.writeUTF("$service,Join,${ib_id!!.text},${ib_pw!!.text},${ib_phone!!.text},${ib_name!!.text}")
                    }
                }.start()

            }
            false
        }
        ib_checkIdBtn!!.setOnClickListener {
            if (dos != null) {
                object : Thread() {
                    override fun run() {
                        dos!!.writeUTF("$service,CheckId,${ib_id!!.text},,,")
                    }
                }.start()
            }
            false
        }
    }

    fun mainGo(v: View){
        var intent = Intent(this,MainActivity::class.java)
        intent.putExtra("service",service)
        startActivity(intent)
    }

    override fun execute(msg: String) {
        if(msg == "true"){
            Toast.makeText(this@InputBox,"회원가입 성공",Toast.LENGTH_SHORT).show();
            var intent = Intent(this@InputBox,LoginBox::class.java)
            intent.putExtra("service",service)
            startActivity(intent)

        }else if(msg == "false"){
            Toast.makeText(this@InputBox,"회원가입 실패",Toast.LENGTH_SHORT).show();
        }else if(msg == "iDtrue"){
            //iDtrue :아이디 중복성체크 허용
            Toast.makeText(this@InputBox,"아이디 사용 가능",Toast.LENGTH_SHORT).show();
            idCheckB=true
        }else if(msg == "iDfalse"){
            //iDtrue :아이디 중복성체크 불가
            Toast.makeText(this@InputBox,"아이디 사용 불가능",Toast.LENGTH_SHORT).show();
            idCheckB=false
        }
    }
}