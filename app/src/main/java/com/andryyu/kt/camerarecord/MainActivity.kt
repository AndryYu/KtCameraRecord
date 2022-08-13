package com.andryyu.kt.camerarecord

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.andryyu.kt.camerarecord.camerax.CameraxRecordActivity
import com.andryyu.kt.camerarecord.ffmpeg.FfmpegRecordActivity
import com.andryyu.kt.camerarecord.glsurfaceview.GLSurfaceRecordActivity
import com.andryyu.kt.camerarecord.surfaceview.SurfaceRecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    companion object{
       const val REQUEST_PERMISSION_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<AppCompatButton>(R.id.btn_surfaceView).setOnClickListener {
            startActivity(Intent(this, SurfaceRecordActivity::class.java))
        }
        findViewById<AppCompatButton>(R.id.btn_opengl).setOnClickListener {
            startActivity(Intent(this, GLSurfaceRecordActivity::class.java))
        }
        findViewById<AppCompatButton>(R.id.btn_ffmpeg).setOnClickListener {
            startActivity(Intent(this, FfmpegRecordActivity::class.java))
        }
        findViewById<AppCompatButton>(R.id.btn_camerax).setOnClickListener {
            startActivity(Intent(this, CameraxRecordActivity::class.java))
        }
        checkPermission()
    }

    /**
     * 检查权限
     */
    private fun checkPermission() {
        if (needPermissionRequest()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList, REQUEST_PERMISSION_CODE
            )
        }
    }

    /**
     * 是否需要申请权限
     */
    private fun needPermissionRequest(): Boolean {
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(this.applicationContext, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && !handleAllGrant(grantResults)) {
            AlertDialog.Builder(MainActivity@ this).apply {
                setTitle("提示")
                setMessage("没有获取到相应权限")
                setCancelable(false)
                setPositiveButton("退出") { _, _ ->
                    finish()
                }
                show()
            }
        }
    }

    /**
     * 检查是否有全部权限
     */
    private fun handleAllGrant(grantResults: IntArray): Boolean {
        for (grant in grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true
    }
}