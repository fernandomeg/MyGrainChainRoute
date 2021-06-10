package com.fmg.mygrainchainroute.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.fmg.mygrainchainroute.AbstractActivity
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.databinding.ActivitySplashScreenBinding
import com.fmg.mygrainchainroute.view.customcontrols.ModalFragment
import java.util.HashMap

class SplashScreenActivity : AbstractActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var animationTitle: Animation
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Validation to App Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermissionsSuccess()){
                startUI()
            }
        }else{
            startUI()
        }
    }

    private fun startUI(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        animationTitle = AnimationUtils.loadAnimation(this,
            R.anim.splash_animation_title
        )
        binding?.tvTitle?.animation = animationTitle

        startSplash()
    }

    private fun startSplash(){
        val splash = object :Thread(){
            override fun run() {
                try{
                    sleep(3000)
                    goToMain()
                }catch (e: Exception){
                    Log.e(e.localizedMessage,e.message.toString())
                }
            }
        }
        splash.start()
    }

    //GET PERMISSIONS APP
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            val perms: MutableMap<String, Int> =
                HashMap()
            addPermissionsList(perms, permissions, *grantResults)
            if (!isPermissionsEnabled(perms)) {
                var isPermissionDenied = false
                for (i in permissions.indices) {
                    if (!shouldShowRequestPermissionRationale(permissions[i]!!) && grantResults[i] == PackageManager.PERMISSION_DENIED
                    ) {
                        isPermissionDenied = true
                    }
                }
                if (isPermissionDenied) {

                    showDialog(false,false,getString(R.string.title_app_name),
                        getString(R.string.permissions_warning_message),
                        object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                            override fun onCancel() {
                                finish()
                            }

                            override fun onAccept() {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:$packageName")
                                )
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        })
                    return
                }

                showDialog(false,false,getString(R.string.title_app_name),
                    getString(R.string.permissions_warning_message2),
                    object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                        override fun onCancel() {
                            finish()
                        }

                        override fun onAccept() {
                            checkPermissionsSuccess()
                        }
                    })
            } else {
                startUI()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}