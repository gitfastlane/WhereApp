package com.example.project
import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

//위치정보 클래스
class MyPlaceP(home:Home,
               title:String,
               latitude:Double,
               longitude:Double,
               color:Int
                )    {

    var latLng: LatLng
    var obj:ObjRendererP
    var title:String
    init{
        latLng = LatLng(latitude,longitude)
        obj = ObjRendererP(home, "andy.obj", "andy.png")
        val ccArr = floatArrayOf(
            Color.red(color)/255f,
            Color.green(color)/255f,
            Color.blue(color)/255f,
            0.5f
        )
        obj.setColorCorrection(ccArr)
        this.title = title
    }
}