package com.fmg.mygrainchainroute

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun convertByteArrayToBitmap(bytes:ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0,bytes.size)
    }

}