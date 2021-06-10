package com.fmg.mygrainchainroute.view.customcontrols

import android.app.Activity
import android.app.AlertDialog
import com.fmg.mygrainchainroute.R

class ProgressLoader(activity: Activity) {
    lateinit var activity: Activity
    var alertDialog: AlertDialog? = null

    init {
        this.activity = activity
    }

    fun showProgressLoader(){
        var builder = AlertDialog.Builder(activity)
        var inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.layout_loader,null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun dismissProgressLoader(){
        if(alertDialog!=null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

}