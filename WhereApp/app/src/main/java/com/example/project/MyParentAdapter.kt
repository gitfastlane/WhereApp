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

class MyParentAdapter(context: Context, userID:String, msg:String):BaseAdapter() , MyExec {
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
        var myParView = MyParentView(context)

        myParView.setting(arr.get(position))
        return myParView
    }

    override fun execute(msg: String) {
    }
}