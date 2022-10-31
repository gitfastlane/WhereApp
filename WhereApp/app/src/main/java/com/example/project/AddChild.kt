package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.io.DataOutputStream

class AddChild : AppCompatActivity(), MyExec {
    var childIdTxt: EditText? = null
    var outIdTxt: TextView? = null
    var outNameTxt: TextView? = null
    var updateBtn:Button? = null
    var outLayout:LinearLayout? = null

    var c_id:String? = null
    var c_name:String = ""
    var message:String? = null
    var userID:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_child)

        childIdTxt = findViewById(R.id.childIdTxt)
        outIdTxt = findViewById(R.id.outIdTxt)
        outNameTxt = findViewById(R.id.outNameTxt)
        updateBtn = findViewById(R.id.updateBtn)
        outLayout = findViewById(R.id.outLayout)

        userID = intent.getStringExtra("userID")

        // rcTh로 변경할 것!!
        //rcTh.activity = this
        rcTh.exec = this

        findViewById<Button>(R.id.searchBtn).setOnClickListener {
            if(childIdTxt!!.text.toString()!=null){
                var dos:DataOutputStream? = rcTh.dos
                if(dos!=null){
                    object:Thread(){
                        override fun run() {
                            Log.d("socket","writeUTF 전 입니다.")
                            dos!!.writeUTF("child,SearchChild,"+childIdTxt!!.text.toString()+",,,")
                            Log.d("socket","writeUTF 후 입니다.")
                        }
                    }.start()
                }
            }
        }

        updateBtn!!.setOnClickListener {
            var dos:DataOutputStream? = rcTh.dos
            if(dos!=null){
                object:Thread(){
                    override fun run() {
                        Log.d("socket","writeUTF 전 입니다.")
                        dos!!.writeUTF("family,InsertChild,"+userID+","+c_id+",0")
                        Log.d("socket","writeUTF 후 입니다.")
                    }
                }.start()
            }

        }
        findViewById<Button>(R.id.homeBtn).setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
        }
    }

    override fun execute(msg:String) {
        runOnUiThread {
            if(msg!=null){
                if(msg.indexOf(",")>0){
                    var strArr = msg.split(",")
                    c_id = strArr[0]
                    c_name = strArr[2]
                    outIdTxt!!.text = c_id
                    outNameTxt!!.text = c_name
                    outLayout!!.visibility = View.VISIBLE
                }else{
                    message = msg
                    if(msg=="true"){
                        val intent = Intent(this, ChildCheck::class.java)
                        intent.putExtra("userID",userID)
                        intent.putExtra("c_name",c_name)
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext,"이미 추가되어있습니다.",Toast.LENGTH_LONG).show()
                    }
                }
                Log.d("socket","child : ${msg}")
            }
        }
    }

}