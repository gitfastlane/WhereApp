package com.example.project

import android.graphics.Color

class MapPinAR {
    var latitude:Double = 0.0
    var longitude:Double = 0.0

    fun makePlaces(arCamera:ARCamera):MyPlaceAR{
        var place = MyPlaceAR(arCamera,"initPin",latitude,longitude, Color.rgb(255,0,0))
        return place
    }
}