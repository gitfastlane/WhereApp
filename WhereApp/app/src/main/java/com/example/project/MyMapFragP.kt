package com.example.project

import android.graphics.*
import android.opengl.Matrix
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.ar.core.Earth
import java.util.*
import kotlin.collections.ArrayList

class MyMapFragP(var home:Home, var googleMap:GoogleMap) {

    var me:Marker
    var you:Marker
    var firstUpdateMe = true
    var earth: Earth  //arCore에서 지도상의 위치 계산할 객체



    // 지도에 표시할 위치들
    var places:ArrayList<MyPlaceP>


    init{
        me = createMarker(Color.GREEN) //내위치 표시
        you = createMarker(Color.RED)

        places = ArrayList()

        /*
        37.50083374956637 , 127.02610078325519
        37.50083375152331 , 127.02610079002386
        37.50083375348027 , 127.02610079679255
        37.50083375543722 , 127.02610080356122
        37.50083375739418 , 127.0261008103299
         37.50083375935113 , 127.02610081709858
        37.50083376213144 , 127.02610082500445
        37.50083376491174 , 127.02610083291033
        37.50083376769205 , 127.0261008408162
        */
        places.add(MyPlaceP(home,"학원뒤",37.50044475543722 , 127.02550080356122,Color.GREEN))
        places.add(MyPlaceP(home,"학원건너편",37.50097475543722 , 127.02561080356122,
            Color.rgb(255,200,0)
            ))


        earth = home.mSession!!.earth!!

        home.runOnUiThread{
            //지도에 띄우기
            for(place in places){
                googleMap.addMarker(MarkerOptions().position(place.latLng).title(place.title))
            }

        }
        placeGLSurfaceMarking()

        //구글맵 클릭 인지
//        googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener { latLng ->
//            Log.d("googleMap 클릭여 ","${latLng}")
//
//            you.setVisible(true)
//            you.position = latLng
//            //you.rotation = 45f
//            youRotation()
//
//        })

        //각종 Gesture 사용금지지
//       val uiSettings = googleMap.uiSettings
//        uiSettings.isMapToolbarEnabled = false
//        uiSettings.isIndoorLevelPickerEnabled = false
//        uiSettings.isZoomControlsEnabled = false
//        uiSettings.isScrollGesturesEnabled = false
//        uiSettings.isTiltGesturesEnabled = false

    }


    fun youRotation(){ //you 가 me 방향으로 바라보게 회전
        if(you != null && you.position != null) {
            val dist = 180 * Math.atan2(
                me.position.longitude - you.position.longitude,
                me.position.latitude - you.position.latitude
            ) / Math.PI
            Log.d("youRotation 여 ", "${dist}")
            you.rotation = dist.toFloat()
        }
    }

    fun placeGLSurfaceMarking(){
        //지정좌표들 서페이스에 띄우기->modelMatrix
        var scale = 50f
        for(place in places){
            val matrix = earthToMatrix(place.latLng)
            Matrix.scaleM(matrix,0,scale,scale,scale)
            place.obj.setModelMatrix(matrix)
        }
    }


    //지리정보(위경도)->Matrix
   fun earthToMatrix(latLng:LatLng):FloatArray{


        val altitude = earth.cameraGeospatialPose.altitude
        //Log.d("여 "+latLng, "${altitude}")
        val anchor = earth.createAnchor(
           latLng.latitude, latLng.longitude,altitude, 0f,0f,0f,1f
       )


        val matrix = FloatArray(16)
        anchor.pose.toMatrix(matrix,0)
       // Log.d("여 "+latLng, Arrays.toString(matrix))
        anchor.detach() //앵커해제
        return matrix
   }




    //마커만들기
    fun createMarker(color:Int): Marker {

        //변경할 마커모양
        var bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(colorBitmap(color))

        val markerOptions = MarkerOptions()
            .position(LatLng(0.0,0.0))
            .draggable(false)
            .anchor(0.5f, 0.5f)
            .flat(true)
            .visible(false)
            .icon(bitmapDescriptor) //기본모양이 아닌 다른 모양 설정

        return googleMap.addMarker(markerOptions)!!
    }


    //비트맵만들기
    fun colorBitmap(color:Int):Bitmap{
        val options = BitmapFactory.Options()
        options.inMutable = true

        //이미지로드
        var res = BitmapFactory.decodeResource(
            home.resources,
            R.drawable.ic_navigation_white_48dp,
            //화살표 모양
           // R.drawable.ic_navigation_white_48dp,
            options
        )

        //크기변경
        val size = 100
        res = Bitmap.createScaledBitmap(res,size, size, false)

        //색상변경
        val pp = Paint()
        pp.colorFilter = LightingColorFilter(color,1)
        val canvas = Canvas(res)
        canvas.drawBitmap(res,0f,0f,pp)
        return res
    }

    fun updateMapMe(latitude:Double, longitude:Double, heading:Double){

        //위치
        val position = LatLng(latitude,longitude)


                                                        //배율,           위치
        var carmeraBuilder = CameraPosition.Builder().zoom(20f).target(position)
        var carmeraUpdate = CameraUpdateFactory.newCameraPosition(carmeraBuilder.build())
        //화면 갱신을 하겠다
        home.runOnUiThread{

            //처음일때만 그리기
            if(firstUpdateMe) {
                firstUpdateMe = false

                //내마커 보이게 하기
                me.isVisible = true
                me.position = position
                me.rotation = heading.toFloat()

                //map이 보여지는 화면을 바꾼다
                googleMap.moveCamera(carmeraUpdate)
            }
            youRotation() //map 에서 터치한 마커의 회전
        }

        placeGLSurfaceMarking()

    }

    //MyFrameLayout에서 더블클릭하여 호출되는 함수

    fun frameTouchGo(point:Point){
        val latLng = googleMap.projection.fromScreenLocation(point)

        you.setVisible(true)
        you.position = latLng

        youRotation()
    }


    fun frameTouchGo2(latLng: LatLng){
        Log.d("frameTouchGo2 여","${latLng}")
        you.setVisible(true)
        you.position = latLng

        youRotation()
    }
}