package com.fmg.mygrainchainroute.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fmg.mygrainchainroute.Constants
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.view.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class RouteTrackingService: LifecycleService() {

    var isFirstStart = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var timeInRouteInSeconds = MutableLiveData<Long>()

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRoute = 0L
    private var timeStarted = 0L
    private var lastSecondStamp = 0L

    private var finishService = false

       companion object{
            val timeInRouteInMillis = MutableLiveData<Long>()
            val isStartRoute = MutableLiveData<Boolean>()
            val pathPoints = MutableLiveData<Polylines>()
       }

    private fun startTimer(){
        addEmptyPolyline()
        isStartRoute.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isStartRoute.value!!){
                //Time to difference between now and TimeStarted
                lapTime = System.currentTimeMillis() - timeStarted

                //post the new lapTime
                timeInRouteInMillis.postValue(timeRoute + lapTime)

                if(timeInRouteInMillis.value!! >= lastSecondStamp + 1000L){
                    timeInRouteInSeconds.postValue(timeInRouteInSeconds.value!! + 1)
                    lastSecondStamp += 1000L
                }
                delay(Constants.TIME_UPDATE_INTERVAL)
            }//end while

            timeRoute += lapTime
        }
    }

    private fun postInitialValues(){
        isStartRoute.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeInRouteInSeconds.postValue(0L)
        timeInRouteInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isStartRoute.observe(this, Observer {
            updateLocationRoute(it)
        })
    }

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            if(isStartRoute.value!!){
                result.locations?.let { locations ->
                    for(location in locations){
                        addPathPoint(location)
                        Log.i(RouteTrackingService::class.java.toString(),"${location.latitude}"
                                +" "+ "${location.longitude}")
                    }
                }
            }//end if
        }//end onLocationresult

    }

    @SuppressLint("MissingPermission")
    private fun updateLocationRoute(isStartRoute:Boolean){
        if(isStartRoute){
            val request = LocationRequest().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.LOCATION_FASTEST_INTERVAL
                priority = PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun addPathPoint(location:Location){
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }

    }

    private fun addEmptyPolyline() =  pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private fun finishService(){
        finishService = true
        isFirstStart = true
        stopForeground(true)
        stopSelf()
        postInitialValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                Constants.ACTION_START_OR_RESUME_SERVICE->{
                    if(isFirstStart) {
                        startForegroundService()
                        isFirstStart = false
                    }else{
                        Log.i(RouteTrackingService::class.toString(), "START or resumed service")
                        startTimer()
                    }
                }
                Constants.ACTION_PAUSE_OR_RESUME_SERVICE->{
                   pauseService()
                    Log.i(RouteTrackingService::class.toString(), "PAUSE or resumed service")
                }
                Constants.ACTION_STOP_OR_RESUME_SERVICE->{
                    Log.i(RouteTrackingService::class.toString(), "STOP or resumed service")
                    finishService()
                }else->{
                    Log.i(RouteTrackingService::class.toString(), "Unknown action service")
                }

            }//end When
        }//endIntent

        return super.onStartCommand(intent, flags, startId)
    }

    private fun pauseService(){
        isStartRoute.postValue(false)
    }

    private fun startForegroundService(){
        startTimer()
        isStartRoute.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            buildNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_navigation_24)
            .setContentTitle("Ruta Iniciada")
            .setContentIntent(getActivityPendingIntent())

        startForeground(Constants.NOTIFICATION_ID,notificationBuilder.build())
    }

    private fun getActivityPendingIntent() = PendingIntent.getActivity(this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_ROUTE_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun buildNotificationChannel(notificationManager: NotificationManager){
         val channel = NotificationChannel(
             Constants.NOTIFICATION_CHANNEL_ID,
             Constants.NOTIFICATION_CHANNEL_NAME,
             IMPORTANCE_LOW
         )
        notificationManager.createNotificationChannel(channel)
    }
}