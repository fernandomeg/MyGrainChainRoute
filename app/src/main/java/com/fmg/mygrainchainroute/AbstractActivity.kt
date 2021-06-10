package com.fmg.mygrainchainroute

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.fmg.mygrainchainroute.view.customcontrols.ModalFragment
import com.fmg.mygrainchainroute.view.customcontrols.ProgressLoader
import java.util.*

abstract class AbstractActivity : AppCompatActivity() {
    //NOSONAR
    protected var loader: ProgressLoader = ProgressLoader(this)
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun showProgressLoader() {
        runOnUiThread {
            if (loader != null) {
                loader.showProgressLoader()
            }
        }
    }

    fun dismissProgressLoader(){
        loader.dismissProgressLoader()
    }

    fun showDialog(esDeError: Boolean, tieneCancelar: Boolean, cabecera: String,
        cuerpo: String, callback: ModalFragment.CommonDialogFragmentCallBack?) {
        ModalFragment.DialogBuilder().setTitle(cabecera)
            .setBody(cuerpo)
            .setSingleButton(!tieneCancelar)
            .setAccept(getString(R.string.title_accept))
            .setCancel(getString(R.string.title_cancel))
            .setCanceledOnTouchOutside(false)
            .addIsError(esDeError)
            .ponInterfaceCallBack(callback)
            .build()
            .show(supportFragmentManager, "TAG MODAL")
    }

    @TargetApi(Build.VERSION_CODES.M)
     fun checkPermissionsSuccess():Boolean {
        val permissionsList: MutableList<String> =
            ArrayList()
        addPermissions(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)
        addPermissions(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)
        addPermissions(permissionsList, Manifest.permission.READ_PHONE_STATE)
        addPermissions(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)
        addPermissions(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        addPermissions(permissionsList, Manifest.permission.ACTIVITY_RECOGNITION)
        if (!permissionsList.isEmpty()) {
            requestPermissions(
                permissionsList.toTypedArray(),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
            return false
        } else {
            return true
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun addPermissions(
        permissionsList: MutableList<String>,
        permission: String
    ) {
        if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            shouldShowRequestPermissionRationale(permission)
        }
    }

     fun addPermissionsList(
        perms: MutableMap<String, Int>,
        permissions: Array<String>,
        vararg grantResults: Int
    ) {
        perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.ACCESS_COARSE_LOCATION] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
         perms[Manifest.permission.ACCESS_BACKGROUND_LOCATION] = PackageManager.PERMISSION_GRANTED
        for (i in permissions.indices) {
            perms[permissions[i]] = grantResults[i]
        }
    }

     fun isPermissionsEnabled(permissions: Map<String, Int>): Boolean {
         return permissions[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED &&
                permissions[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED &&
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED &&
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED &&
                permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == PackageManager.PERMISSION_GRANTED
    }


}