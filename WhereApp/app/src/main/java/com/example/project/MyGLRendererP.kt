package com.example.project


import android.opengl.GLES30
import android.opengl.GLSurfaceView


import com.google.ar.core.Session

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRendererP(private  val mContext: Home):GLSurfaceView.Renderer {


    //화면변화유무
    var viewportChange = false
    var width = 0
    var height = 0

    var mCamera:CarmeraRendererP?

    var obj:ObjRendererP

    fun updateSession(session: Session, rotation:Int){
        //화면이 변하면
        if(viewportChange){

            //세션 화면 설정 <- openGL 에서 화면 크기, 회전 정보를 가져와 셋팅한다
            session.setDisplayGeometry(rotation, width,height)
            viewportChange = false
        }
    }

    //생성자
    init {
        mCamera = CarmeraRendererP()

        obj = ObjRendererP(mContext,"andy.obj","andy.png")

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)  //3D 입체로 보이게 함
        GLES30.glClearColor(1f, 0.6f, 0.6f, 1f)
        mCamera!!.init()

        obj.init()
        if(mContext.myMapFragP != null){
            for(place in mContext.myMapFragP!!.places){
                place.obj.init()
            }
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        viewportChange = true
        this.width = width
        this.height = height
        GLES30.glViewport(0,0,width,height)

    }


    //카메라렌더러ID 리턴
    val textureID:Int
        get() = if(mCamera==null) -1 else mCamera!!.mTextures!![0]


    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        mContext.preRender() //그리기 전에 mainActivity의 preRender() 실행

        GLES30.glDepthMask(false)
        mCamera!!.draw()
        GLES30.glDepthMask(true)


        obj.draw()

        if(mContext.myMapFragP != null){
            for(place in mContext.myMapFragP!!.places){
                place.obj.draw()
            }
        }

    }

    fun updateViewMatrix(matrix: FloatArray){

        obj.setViewMatrix(matrix)
        if(mContext.myMapFragP != null){
            for(place in mContext.myMapFragP!!.places){
                place.obj.setViewMatrix(matrix)
            }
        }

    }

    fun updateProjMatrix(matrix: FloatArray){

        obj.setProjectionMatrix(matrix)
        if(mContext.myMapFragP != null){
            for(place in mContext.myMapFragP!!.places){
                place.obj.setProjectionMatrix(matrix)
            }
        }
    }


}