package com.example.project

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.Socket

class ReceiverThread():Thread() {

    var dos:DataOutputStream? = null
    var socket: Socket? = null
    var msg:String? = null

    // 서버연결후 데이터 통신을 할때에 반드시 선언해줘야한다.
    var exec:MyExec? = null
    var activity:AppCompatActivity? = null
    init {
       // this.activity = activity
       // this.exec = exec
    }


    override fun run() {
        //192.168.200.103 집
        //192.168.0.116 학원
        socket = Socket("192.168.100.27",7777)
       // socket = Socket("192.168.0.116",7777)
        Log.d("socket","접속 성공입니다.")

        dos = DataOutputStream(socket!!.getOutputStream())
        var dis = DataInputStream(socket!!.getInputStream())

        try {
            while (dis!=null){
                Log.d("socket","readUTF() 전 입니다.")
                msg = dis.readUTF()
                activity!!.runOnUiThread {
                    exec!!.execute(msg!!)
                }

                Log.d("socket","msg = ${msg}")
            }
        }catch (e: Exception){
            Log.d("socket 애러",e.toString())
        }finally {
            //Log.d("socket","사망")
            dis.close()
            dos!!.close()
            socket!!.close()
            //dos=null
        }
    }
}