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

class ParentAdapter(context: Context, userID:String, msg:String):BaseAdapter() , MyExec {
    var arr = mutableListOf<Parent>()
    var context:Context
    var userID:String? = null
    init {
        this.context = context
        this.userID = userID
        arr = createArr(msg)
        rcTh!!.exec = this
    }

    fun createArr(msg:String):MutableList<Parent>{
        var arrParent = mutableListOf<Parent>()
        var list = msg.split(",")
        var ch:Parent? = null
        for (i in 0 until list.size){
            if(i%2==0){
                ch = Parent()
                ch.p_id = list[i]
            }else{
                ch!!.p_name = list[i]
                arrParent.add(ch)
            }
        }
        return arrParent
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
        var parView = ParentView(context)

        var parentOkBtn = parView.findViewById<Button>(R.id.parentOkBtn)
        var parentId = arr.get(position).p_id
        parentOkBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("부모 수락")
                .setMessage("수락하시려면 확인을 눌러주세요.")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, i ->
                    object:Thread(){
                        override fun run() {
                            Log.d("socket","writeUTF 전 입니다.")
                            rcTh.dos!!.writeUTF("family,ConfirmParent,"+parentId+","+userID+",0")
                            Log.d("socket","writeUTF 후 입니다.")
                        }
                    }.start()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, i ->

                })
            builder.show()
        }
        parView.setting(arr.get(position))
        return parView
    }

    override fun execute(msg: String) {
        Log.d("socket","parentAdapter execute 입니다.")
        arr = createArr(msg)

        notifyDataSetChanged()
    }
}