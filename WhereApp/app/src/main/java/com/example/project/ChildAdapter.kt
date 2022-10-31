package com.example.project

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog



class ChildAdapter(context: Context, userID:String, msg:String):BaseAdapter() , MyExec {
    var arr = mutableListOf<Child>()
    var context:Context
    var userID:String? = null
    init {
        this.context = context
        this.userID = userID
        arr = createArr(msg)
        rcTh!!.exec = this
    }

    fun createArr(msg:String):MutableList<Child>{
        var arrChild = mutableListOf<Child>()
        var list = msg.split(",")
        var ch:Child? = null
        for (i in 0 until list.size){
            if(i%2==0){
                ch = Child()
                ch.c_id = list[i]
            }else{
                ch!!.c_name = list[i]
                arrChild.add(ch)
            }
        }
        return arrChild
    }

    override fun getCount(): Int {
        return arr.size
    }

    override fun getItem(position: Int): Any {
        return arr.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var chiView = ChildView(context)

        val btn = chiView.findViewById<Button>(R.id.parentOkBtn)
        val childID = arr.get(position).c_id

        btn.setOnClickListener{
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("자식 연결")
                .setMessage("연결을 원하시면 확인을 눌러주세요.")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, i ->

                    var intent = Intent(context,Home::class.java)
                    intent.putExtra("childID",childID)
                    intent.putExtra("userID",userID)
                    context.startActivity(intent)

//                    object:Thread(){
//                        override fun run() {
//                            Log.d("socket","writeUTF 전 입니다.")
//                            rcTh.dos!!.writeUTF("")
//                            Log.d("socket","writeUTF 후 입니다.")
//                        }
//                    }.start()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, i ->

                })
            builder.show()



        }

        chiView.setting(arr.get(position))
        return chiView
    }

    override fun execute(msg: String) {

    }

}