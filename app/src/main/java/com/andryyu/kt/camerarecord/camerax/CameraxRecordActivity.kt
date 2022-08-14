package com.andryyu.kt.camerarecord.camerax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.andryyu.kt.camerarecord.R

class CameraxRecordActivity : AppCompatActivity() {
    private var mPreviewView:PreviewView ?= null
    private var mImageCapture:ImageCapture ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax_record)

        mPreviewView = findViewById(R.id.pv_record)
        initCameraX()
    }

    /**
     * 初始化camerax
     */
    private fun initCameraX(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            ////将相机的生命周期和activity的生命周期绑定，camerax 会自己释放，不用担心了
            val cameraProvider = cameraProviderFuture.get()
            //预览的 capture，它里面支持角度换算
            val preview = Preview.Builder().build()

            mImageCapture = ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .build()
            preview.setSurfaceProvider(mPreviewView!!.surfaceProvider)

            //选择后置摄像头
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            //预览之前先解绑
            cameraProvider.unbindAll()

            //将数据绑定到相机的生命周期中
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector, preview, mImageCapture)
        },ContextCompat.getMainExecutor(this))
    }
}