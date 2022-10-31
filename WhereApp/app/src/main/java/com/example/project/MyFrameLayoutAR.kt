package com.example.project


import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout

//생성시 반드시 mainActivity: Context, attrs:AttributeSet? 이렇게 받을것
//mainActivity: MainActivity 형변환 못해서 부모생성자 오류 발생
class MyFrameLayoutAR(arCamera: Context, attrs:AttributeSet?) :FrameLayout(arCamera, attrs) {
    var mGestureDetector : GestureDetector

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(ev)

        return false
    }

    init {

        val activity = arCamera as ARCamera
        mGestureDetector = GestureDetector(arCamera, object :GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent): Boolean {
                Log.d("onDoubleTap 여","${e.x}, ${e.y}")

                val pp = Point(e.x.toInt(), e.y.toInt())
                activity.myMapFragAR!!.frameTouchGo(pp)
                //터치 이벤트를 레이아웃 객체에서 실행하지 않는다.
                return false
            }
        })
    }

}