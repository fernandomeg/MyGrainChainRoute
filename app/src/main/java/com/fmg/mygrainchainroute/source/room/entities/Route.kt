package com.fmg.mygrainchainroute.source.room.entities

import android.graphics.Bitmap
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "route")
data class Route (
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var routeId: Int=0,
    @Nullable
    var map: Bitmap?=null,
    @NotNull
    var nameRoute: String="",
    @NotNull
    var timeStamp: Long=0L,
    @NotNull
    var distanceInMeters:Int=0,
    @NotNull
    var timeInMillis:Long=0L
)