package com.fmg.mygrainchainroute

import android.location.Location
import com.fmg.mygrainchainroute.services.Polyline
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object Utilities {

    fun convertTimeStampInDate(timeStamp:Long):String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeStamp
        }

        return dateFormat.format(calendar.time)
    }

    fun convertMetersInKilometers(meters:Int): Float{
        return String.format("%.3f", (meters.toFloat()/1000F)).toFloat()
    }


    fun calculatePolyline(polyline: Polyline):Float{
        var distance =0f
        for(i in 0..polyline.size-2){
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun getFormattedStopWatchTime(ms:Long, includeMillis:Boolean= false):String{
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val second = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        if(!includeMillis){
            return "${if(hours <10) "0" else ""}$hours:"+
                    "${if(minutes <10 ) "0" else ""}$minutes:"+
                    "${if(second <10 ) "0" else ""}$second"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(second)
        milliseconds /= 10

        return "${if(hours <10 ) "0" else ""}$hours:"+
                "${if(minutes <10 ) "0" else ""}$minutes:"+
                "${if(second <10 ) "0" else ""}$second"+
                "${if(milliseconds <10 ) "0" else ""}$milliseconds"
    }

}