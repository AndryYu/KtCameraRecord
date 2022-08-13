package com.andryyu.kt.camerarecord.surfaceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.andryyu.kt.camerarecord.R

class SurfaceRecordActivity : AppCompatActivity() ,SurfaceHolder.Callback{
    private var mSurfaceView:SurfaceView?=null
    var mSurfaceHolder:SurfaceHolder?=null

    companion object{
        const val TAG = "SurfaceRecordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_record)

        initView()

    }

    private fun initView(){
        mSurfaceView = findViewById(R.id.sv_record)
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder!!.setKeepScreenOn(true)
        mSurfaceHolder!!.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG,"surfaceCreated")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG,"surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG,"surfaceDestroyed")
    }
}