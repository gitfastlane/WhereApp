package com.example.project

import android.Manifest

import android.content.Intent
import android.hardware.display.DisplayManager
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.*
import java.io.DataOutputStream
import kotlin.concurrent.thread


class Home : AppCompatActivity(),MyExec {

    var userID:String? = null //유저 아이디
    var childID:String? = null //유저 아이디

    var myBtn:Button? = null
    var myChildListBtn:Button? = null
    var alarmBtn:ImageButton? = null

    //Ar화면
    var glSurfaceP:GLSurfaceView ?= null
    var mRendererP:MyGLRendererP ?= null
    var myMapFragP:MyMapFragP ?= null


    //자식 좌표 (추가)
    var childLatitude:Double ?= null
    var childLongitude:Double ?= null
    var childHeading:Double ?= null

    //스레드 플레그
    var falg1:Boolean = true

    // 어떤 옵션으로갈지 선택
    var option:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 서버
        rcTh.activity = this
        rcTh.exec = this
        var dos = rcTh.dos

        //ARMap
        glSurfaceP = findViewById(R.id.glSufaceP)
        var mapFragP = supportFragmentManager.findFragmentById(R.id.myMapP) as SupportMapFragment


        mapFragP!!.getMapAsync{  googlMap ->
            myMapFragP = MyMapFragP(this,googlMap)

        }

        mRendererP = MyGLRendererP(this)
        glSurfaceP!!.setEGLContextClientVersion(3)
        glSurfaceP!!.preserveEGLContextOnPause=true
        glSurfaceP!!.setRenderer(mRendererP)
        glSurfaceP!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

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
                        mRendererP!!.viewportChange = true
                        // Log.d("화면변경이여 : ",  "" + mSession)
                    }
                }

            }, null

        )




        if(intent.getStringExtra("userID")!=null){
            userID = intent.getStringExtra("userID")
        }
        if(intent.getStringExtra("childID")!=null){
            childID = intent.getStringExtra("childID")
        }
        Log.d("childID 여","$childID")

        myBtn = findViewById(R.id.myBtn)
        myChildListBtn = findViewById(R.id.myChildListBtn)
        alarmBtn = findViewById(R.id.alarmBtn)

        // My 버튼 클릭
        myBtn!!.setOnClickListener {
            falg1 = false

            option = "myBtn"
            if(dos!=null){
                Log.d("socket","myBtn 들어옴.")
                object:Thread(){
                    override fun run() {
                        Log.d("socket","init writeUTF 전 입니다.")
                        dos.writeUTF("family,SelectChildList,"+userID+",,0")
                    }
                }.start()
            }
        }

        // 연결관리 버튼 클릭
        myChildListBtn!!.setOnClickListener {
            falg1 = false
            Log.d("socket","연결관리 클릭함")

            option = "myChildListBtn"
            if(dos!=null){
                Log.d("socket","if(dos!=null) 들어옴.")
                object:Thread(){
                    override fun run() {
                        Log.d("socket","init writeUTF 전 입니다.")
                        dos.writeUTF("family,SelectChildList,"+userID+",,0")
                    }
                }.start()
            }
        }

        // 알람 버튼 클릭
        alarmBtn!!.setOnClickListener {

        }
        //좌표 실시간으로 가져오기
        starts()

    }
    var mSession: Session? = null


    override fun onResume() {
        super.onResume()
        if(mSession==null) {
            try {
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
        glSurfaceP!!.onResume()

    }
    override fun onPause() {
        super.onPause()
        glSurfaceP!!.onPause()
        mSession!!.pause()
    }
    fun preRender(){
        //Log.d("MainActivity", "preRender() 여")
        if(mRendererP!!.viewportChange){  //화면 변경이면 세션화면도 변경
            val display = windowManager.defaultDisplay
            mRendererP!!.updateSession(mSession!!, display.rotation)
        }

        //session카메라 텍스처이름을 CameraRenderer.textureID로 지정
        mSession!!.setCameraTextureName(mRendererP!!.textureID)

        //프레임형태로 화면 업데이트
        var frame: Frame?  = null

        try {
            frame = mSession!!.update()
        }catch (e:Exception){

        }

        mRendererP!!.mCamera!!.transformDisplayGeometry(frame!!)


        //현재 카메라의 위치 정보를 얻을 메트릭스를 생성한다.
        val viewMatrix = FloatArray(16)
        val projMatrix = FloatArray(16)

        //카메라로부터 위치 정보를 받는다
        frame!!.camera.getViewMatrix(viewMatrix,0)
        frame!!.camera.getProjectionMatrix(projMatrix,0,0.01f,10000f)

        //포인트클라우드에 카메라로 받은 메트릭스 셋팅
        mRendererP!!.updateViewMatrix(viewMatrix)
        mRendererP!!.updateProjMatrix(projMatrix)


        ///지도 좌표 받기
        val earth = mSession!!.earth
        if(earth!!.trackingState == TrackingState.TRACKING){
            val mePose = earth.cameraGeospatialPose
            // Log.d("earth 여", "${mePose.latitude} , ${mePose.longitude}, ${mePose.heading}  ")
            if(myMapFragP!=null){
                //earth로 부터 받은 내위치 map 갱신
                myMapFragP!!.updateMapMe(mePose.latitude , mePose.longitude,mePose.heading)
            }

        }
    }
    //메소드 전체 스레드
    fun starts(){
        thread {
            while (falg1){
                Thread.sleep(1000)
                start2()
            }
        }
    }
    //상대 좌표 찾기
    fun start2(){
        option = "start2"
        if(childID!=null){
            Log.d("start2실행 여","start2 실행")
            var dos: DataOutputStream?= rcTh.dos
            if(dos!=null){
                object:Thread(){
                    override fun run() {
                        dos!!.writeUTF("location,SearchLocation,$childID,,,")
                    }
                }.start()
            }
        }
    }

    fun ARCameraGo(v:View){
        falg1 = false

        if(childLatitude != null){
            mapPinAR.latitude = childLatitude!!.toDouble()
            mapPinAR.longitude = childLongitude!!.toDouble()
        }
        var intent = Intent(this,ARCamera::class.java)
        intent.putExtra("userID",userID)
        intent.putExtra("childID",childID)

        startActivity(intent)


    }
    override fun execute(msg: String) {
        Log.d("socket","execute 후 입니다.")
        if(option=="myBtn"){
            intent = Intent(this, MyList::class.java)
            intent.putExtra("userID",userID)
            intent.putExtra("childID",childID)
            intent.putExtra("msg",msg)
            startActivity(intent)
        }else if(option=="myChildListBtn") {
            intent = Intent(this, ChildList::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("childID", childID)
            intent.putExtra("msg", msg)
            startActivity(intent)

        }else if(option == "start2"){
            if(msg != null){
                Log.d("locationResult 여","$msg")
                var locationResult = msg.split(",")
                //자식의 좌표
                childLatitude = locationResult[0].toDouble()
                childLongitude = locationResult[1].toDouble()
                childHeading = locationResult[2].toDouble()

                //실시간 좌표가 찍히는 부분
                var pp = LatLng(childLatitude!!,childLongitude!!)
                myMapFragP!!.frameTouchGo2(pp)
            }
        }


    }



}





