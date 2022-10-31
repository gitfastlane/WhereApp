package com.example.project

import android.graphics.Color

class MapPinC {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    fun makePlaces(mainActivity:HomeChild):MyPlaceC{
        var place = MyPlaceC(mainActivity,"initPin",latitude,longitude, Color.rgb(255,0,0))
        return place
    }

}