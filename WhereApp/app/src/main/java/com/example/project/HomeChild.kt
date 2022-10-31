package com.example.project

import android.content.Intent
import android.hardware.display.DisplayManager
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ThemedSpinnerAdapter
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.*
import java.io.DataOutputStream
import kotlin.concurrent.thread

class HomeChild : AppCompatActivity(), MyExec {

    var glSurfaceC: GLSurfaceView? = null
    var mRendererC: MyGLRendererC? = null
    var myMapFragC: MyMapFragC? = null
    var mSession: Session? = null

    var childAlarmBtn:ImageButton? = null
    var parentListBtn:ImageButton? = null
    var userID:String? = null

    var option:String? = null
    //나의 좌표
    var latitude:Double ?= null
    var longitude:Double ?= null
    var heading:Double ?= null

    var flag:Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_child)

        rcTh.activity = this
        rcTh.exec = this
        userID = intent.getStringExtra("userID")

        childAlarmBtn = findViewById(R.id.childAlarmBtn)
        parentListBtn = findViewById(R.id.parentListBtn)

        //Map 설정
        glSurfaceC = findViewById(R.id.glSurfaceC)
        val mapFrag = supportFragmentManager.findFragmentById(R.id.myMapC) as SupportMapFragment
        mapFrag!!.getMapAsync{ googleMap ->
            myMapFragC = MyMapFragC(this, googleMap)
        }

        mRendererC = MyGLRendererC(this)
        glSurfaceC!!.setEGLContextClientVersion(3)
        glSurfaceC!!.preserveEGLContextOnPause=true
        glSurfaceC!!.setRenderer(mRendererC)
        glSurfaceC!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        ///화면 변화 감지 --> 화면 회전(onResume ,onPause 인지)
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        displayManager?.registerDisplayListener(
            object : DisplayManager.DisplayListener{
                override fun onDisplayAdded(displayId: Int) { }
                override fun onDisplayRemoved(displayId: Int) { }
                //화면 변경시
                override fun onDisplayChanged(displayId: Int) {
                    synchronized(this){
                        //화면이 변경되었음을 알려준다
                        mRendererC!!.viewportChange = true
                        // Log.d("화면변경이여 : ",  "" + mSession)
                    }
                }
            }, null
        )

        option = "starts"

        // 동작 부분
        childAlarmBtn!!.setOnClickListener {
            flag = false
            option = "childAlarmBtn"
            var dos = rcTh.dos
            if(dos!=null){
                object:Thread(){
                    override fun run() {
                        dos.writeUTF("family,ShowInvite,"+userID+","+userID+",0")
                    }
                }.start()
            }
        }

        parentListBtn!!.setOnClickListener {
            flag = false
            option = "parentListBtn"
            var dos = rcTh.dos
            if(dos!=null){
                object:Thread(){
                    override fun run() {
                        dos.writeUTF("family,SelectParentList,"+userID+","+userID+",0")
                    }
                }.start()
            }
        }
        //좌표 업데이트
        starts()
    }
    override fun onResume() {
        super.onResume()
        if(mSession==null) {
            try {
//                Log.d("onResume세션이여 : ",
//                    "" + ArCoreApk.getInstance().requestInstall(this, true).toString())
                //ar 서비스 설치 요청 --> 설치
                // 구글플레이에 로그인 되어야 가능
                when(ArCoreApk.getInstance().requestInstall(this, true)){
                    //설치가 되었을때 만
                    ArCoreApk.InstallStatus.INSTALLED ->{
                        Log.d("INSTALLED세션이여 : ",  "" + mSession)
                        mSession = Session(this) //ArCore 세션 생성

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
        glSurfaceC!!.onResume()


    }

    override fun onPause() {
        super.onPause()
        glSurfaceC!!.onPause()
        mSession!!.pause()

    }
    fun preRender(){
        Log.d("MainActivity여", "preRender() 여")
        if(mRendererC!!.viewportChange){  //화면 변경이면 세션화면도 변경
            val display = windowManager.defaultDisplay
            Log.d("preRender()여", "${mSession}")
            mRendererC!!.updateSession(mSession!!, display.rotation)
        }
        //session카메라 텍스처이름을 CameraRenderer.textureID로 지정
        mSession!!.setCameraTextureName(mRendererC!!.textureID)
        //프레임형태로 화면 업데이트
        var frame: Frame?  = null
        try {
            frame = mSession!!.update()
        }catch (e:Exception){ }

        mRendererC!!.mCamera!!.transformDisplayGeometry(frame!!)

        //현재 카메라의 위치 정보를 얻을 메트릭스를 생성한다.
        val viewMatrix = FloatArray(16)
        val projMatrix = FloatArray(16)

        //카메라로부터 위치 정보를 받는다
        frame!!.camera.getViewMatrix(viewMatrix,0)
        frame!!.camera.getProjectionMatrix(projMatrix,0,0.01f,10000f)

        //포인트클라우드에 카메라로 받은 메트릭스 셋팅
        mRendererC!!.updateViewMatrix(viewMatrix)
        mRendererC!!.updateProjMatrix(projMatrix)

        ///지도 좌표 받기
        val earth = mSession!!.earth
        if(earth!!.trackingState == TrackingState.TRACKING){
            val mePose = earth.cameraGeospatialPose
            // Log.d("earth 여", "${mePose.latitude} , ${mePose.longitude}, ${mePose.heading}  ")
            if(myMapFragC!=null){
                //earth로 부터 받은 내위치 map 갱신
                myMapFragC!!.updateMapMe(mePose.latitude , mePose.longitude,mePose.heading)
            }
            latitude = mePose.latitude
            longitude = mePose.longitude
            heading = mePose.heading
            Log.d("좌표 여","$latitude,$longitude,$heading")
        }
    }

    fun starts(){
        thread {
            while (flag!!){
                Thread.sleep(3000)
                start()
            }
        }
    }
    //좌표 업데이트
    fun start(){
        var dos: DataOutputStream?= rcTh.dos
        if(flag!!){
            if(dos!=null){
                object:Thread(){
                    override fun run() {
                        dos!!.writeUTF("location,UpdateLocation,$userID,$latitude,$longitude,$heading")
                    }
                }.start()
            }
        }
    }

    override fun execute(msg: String) {
        if(option=="childAlarmBtn"){
            Log.d("socket","HomeChild execute childAlarmBtn")
            var intent = Intent(this,ParentList::class.java)
            intent.putExtra("userID",userID)
            intent.putExtra("msg",msg)
            startActivity(intent)
        }else if(option=="parentListBtn"){
            Log.d("socket","HomeChild execute parentListBtn")
            var intent = Intent(this,MyParentList::class.java)
            intent.putExtra("userID",userID)
            intent.putExtra("msg",msg)
            startActivity(intent)
        }else if(option=="starts"){
            if(msg!=null){
                if(msg=="true"){
                    Log.d("socket 여","child 위치전송: ${msg}")
                }else{
                    Log.d("socket 여","child 위치전송: ${msg}")
                }
            }
        }
    }
}