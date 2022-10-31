package com.example.project

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MemberAdapter(context: Context, userID:String, msg:String):BaseAdapter(),MyExec{

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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var memView = MemberView(context)
        Log.d("socket","arr.get(position) 전 입니다.")

        val btn = memView.findViewById<Button>(R.id.deleteBtn)
        val childId = arr.get(position).c_id
        btn.setOnClickListener{
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("자식 삭제")
                .setMessage("삭제를 원하시면 확인을 눌러주세요.")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, i ->

                    object:Thread(){
                        override fun run() {
                            Log.d("socket","writeUTF 전 입니다.")
                            rcTh.dos!!.writeUTF("family,DeleteChild,"+userID+","+childId+",0")
                            Log.d("socket","writeUTF 후 입니다.")
                        }
                    }.start()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, i ->

                })
            builder.show()
        }

        memView.setting(arr.get(position))
        return memView
    }

    override fun execute(msg: String) {
        Log.d("socket","memberAdapter execute 입니다.")
        arr = createArr(msg)

        notifyDataSetChanged()
    }
}