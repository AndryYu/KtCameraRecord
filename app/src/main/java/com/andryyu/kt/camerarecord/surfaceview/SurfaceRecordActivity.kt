package com.andryyu.kt.camerarecord.surfaceview

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.SessionConfiguration
import android.media.Image
import android.media.ImageReader
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.andryyu.kt.camerarecord.MainActivity
import com.andryyu.kt.camerarecord.R
import java.lang.Exception

class SurfaceRecordActivity : AppCompatActivity() ,SurfaceHolder.Callback,ImageReader.OnImageAvailableListener{
    private var mSurfaceView:SurfaceView?=null
    var mSurfaceHolder:SurfaceHolder?=null
    var mCameraManager:CameraManager ?=null
    var mImageReader:ImageReader ?= null
    var mPrevHandler:Handler ?=null
    var mRecordHandler:Handler ?= null
    var mCameraDevice:CameraDevice ?= null
    var mCameraCaptureSession:CameraCaptureSession ?=null
    var mCameraId:Int = 0 //摄像头 0为后 1为前

    var mStateListener = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            startPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
           closeCamera()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Toast.makeText(applicationContext,"开启摄像头失败",Toast.LENGTH_SHORT).show()
            closeCamera()
        }
    }

    companion object{
        const val TAG = "SurfaceRecordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_record)

        initView()
        initHandler()
        openCamera()
    }

    private fun initView(){
        mSurfaceView = findViewById(R.id.sv_record)
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder!!.setKeepScreenOn(true)
        mSurfaceHolder!!.addCallback(this)
    }

    private fun initHandler(){

        mCameraId = CameraCharacteristics.LENS_FACING_FRONT
        val previewThread = HandlerThread("Camera2Preview")
        previewThread.start()
        val recordThread = HandlerThread("Camera2Record")
        recordThread.start()
        mPrevHandler = Handler(previewThread.looper)
        mRecordHandler= Handler(recordThread.looper)

        mImageReader = ImageReader.newInstance(1080,1920,ImageFormat.JPEG,2)
        mImageReader!!.setOnImageAvailableListener(this,mRecordHandler)
    }

    private fun openCamera(){
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mCameraManager!!.openCamera(mCameraId.toString(),mStateListener,mRecordHandler)
    }

    private fun startPreview(){
        try {
            val captureRequest = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequest.addTarget(mSurfaceHolder!!.surface)
            captureRequest.addTarget(mImageReader!!.surface)
            mCameraDevice!!.createCaptureSession(
                arrayListOf(mSurfaceHolder!!.surface,mImageReader!!.surface),
                object: CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        if (mCameraDevice==null)
                            return
                        mCameraCaptureSession = session
                        mCameraCaptureSession!!.setRepeatingRequest(captureRequest.build(),
                            object: CameraCaptureSession.CaptureCallback() {

                           },mPrevHandler)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                },mPrevHandler
            )
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    override fun onImageAvailable(reader: ImageReader?) {
        val image = reader!!.acquireLatestImage()

        image.close()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG,"surfaceCreated")
        openCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG,"surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG,"surfaceDestroyed")
    }

    private fun closeCamera(){
        mCameraDevice?.close()
        mCameraDevice=null
    }
}
