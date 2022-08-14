package com.andryyu.kt.camerarecord.glsurfaceview

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.andryyu.kt.camerarecord.R
import com.andryyu.kt.camerarecord.view.CircularProgressView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLSurfaceRecordActivity : AppCompatActivity() ,GLSurfaceView.Renderer,SurfaceTexture.OnFrameAvailableListener {

    var mGLSurfaceView:GLSurfaceView ?= null
    var mCircleView:CircularProgressView ?=null
    var mGLCameraDrawer:GLCameraDrawer ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsurface_view_record)

        initView()
        initOpenGL()
    }

    private fun initView(){
        mGLSurfaceView = findViewById(R.id.gsv_record)
        mCircleView = findViewById(R.id.btn_record)
        mCircleView!!.setOnClickListener {

        }

    }

    private fun initOpenGL(){
        //设置版本
        mGLSurfaceView!!.setEGLContextClientVersion(2)
        //设置Render
        mGLSurfaceView!!.setRenderer(this)
        //主动渲染
        mGLSurfaceView!!.renderMode =GLSurfaceView.RENDERMODE_WHEN_DIRTY
        //保存context当pause时
        mGLSurfaceView!!.preserveEGLContextOnPause = true
        //设置相机距离
        mGLSurfaceView!!.cameraDistance = 100f

        mGLCameraDrawer = GLCameraDrawer(resources)
    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        mGLSurfaceView!!.requestRender()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {

    }
}