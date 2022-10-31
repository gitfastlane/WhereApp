package com.example.project

import android.content.Intent
import android.hardware.display.DisplayManager
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import com.google.ar.core.*

class ARCamera : AppCompatActivity() {
    var glsurfaceAR:GLSurfaceView ?= null
    var mRendererAR:MyGLRendererAR ?= null
    var myMapFragAR:MyMapFragAR ?= null

    //andy의 좌표
    var longitude:Double ?= null
    var latitude:Double ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcamera)

        /*
        if(intent.getStringExtra("longitude")!= null){
            longitude = intent.getStringExtra("longitude")!!.toDouble()
            latitude = intent.getStringExtra("latitude")!!.toDouble()
        }

         */

        glsurfaceAR = findViewById(R.id.glSurfaceAR)
        val mapFragAR = supportFragmentManager.findFragmentById(R.id.myMapAR) as SupportMapFragment

        mapFragAR!!.getMapAsync{    googleMapAR ->
            myMapFragAR = MyMapFragAR(this,googleMapAR)
        }

        mRendererAR = MyGLRendererAR(this)

        glsurfaceAR!!.setEGLContextClientVersion(3)
        glsurfaceAR!!.preserveEGLContextOnPause = true
        glsurfaceAR!!.setRenderer(mRendererAR)
        glsurfaceAR!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        ///화면 변화 감지 --> 화면 회전(onResume ,onPause 인지)
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager

        displayManager?.registerDisplayListener(
            object : DisplayManager.DisplayListener{
                override fun onDisplayAdded(displayId: Int) {

                }

                override fun onDisplayRemoved(displayId: Int) {

                }

                //화면 변경시
                override fun onDisplayChanged(displayId: Int) {
                    synchronized(this){
                        //화면이 변경되었음을 알려준다
                        mRendererAR!!.viewportChange = true
                        // Log.d("화면변경이여 : ",  "" + mSession)
                    }
                }

            }, null
        )
    }
    var mSession: Session? = null

    override fun onResume() {
        super.onResume()

        if(mSession==null) {
            //mSession = Session(this) //ArCore 세션 생성


            try {

//                Log.d("세션이여 : ",
//                    "" + ArCoreApk.getInstance().requestInstall(this, true).toString())
                //ar 서비스 설치 요청 --> 설치
                // 구글플레이에 로그인 되어야 가능
                when(ArCoreApk.getInstance().requestInstall(this, true)){
                    //설치가 되었을때 만
                    ArCoreApk.InstallStatus.INSTALLED ->{

                        mSession = Session(this) //ArCore 세션 생성
                        //Log.d("세션이여 : ",  "" + mSession)

                        //arCore 에서 지리정보를 사용하겠다.
                        val config = Config(mSession)
                        config.geospatialMode = Config.GeospatialMode.ENABLED
                        mSession!!.configure(config)
                    }

                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        try {
            mSession!!.resume()
        }catch (e:Exception){
            e.printStackTrace()
        }

        glsurfaceAR!!.onResume()

    }

    override fun onPause() {
        super.onPause()
        glsurfaceAR!!.onPause()
        mSession!!.pause()

    }

    fun preRender(){
        //Log.d("MainActivity", "preRender() 여")
        if(mRendererAR!!.viewportChange){  //화면 변경이면 세션화면도 변경
            val display = windowManager.defaultDisplay
            mRendererAR!!.updateSession(mSession!!, display.rotation)
        }

        //session카메라 텍스처이름을 CameraRenderer.textureID로 지정
        mSession!!.setCameraTextureName(mRendererAR!!.textureID)

        //프레임형태로 화면 업데이트
        var frame: Frame?  = null

        try {
            frame = mSession!!.update()
        }catch (e:Exception){

        }

        mRendererAR!!.mCamera!!.transformDisplayGeometry(frame!!)


        //현재 카메라의 위치 정보를 얻을 메트릭스를 생성한다.
        val viewMatrix = FloatArray(16)
        val projMatrix = FloatArray(16)

        //카메라로부터 위치 정보를 받는다
        frame!!.camera.getViewMatrix(viewMatrix,0)
        frame!!.camera.getProjectionMatrix(projMatrix,0,0.01f,10000f)

        //포인트클라우드에 카메라로 받은 메트릭스 셋팅
        mRendererAR!!.updateViewMatrix(viewMatrix)
        mRendererAR!!.updateProjMatrix(projMatrix)


        ///지도 좌표 받기
        val earth = mSession!!.earth
        if(earth!!.trackingState == TrackingState.TRACKING){
            val mePose = earth.cameraGeospatialPose
            // Log.d("earth 여", "${mePose.latitude} , ${mePose.longitude}, ${mePose.heading}  ")
            if(myMapFragAR!=null){
                //earth로 부터 받은 내위치 map 갱신
                myMapFragAR!!.updateMapMe(mePose.latitude , mePose.longitude,mePose.heading)
            }
        }
    }
}