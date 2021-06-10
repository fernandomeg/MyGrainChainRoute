package com.fmg.mygrainchainroute

import android.graphics.Color

object Constants {

    const val ROUTE_DATABASE_NAME = "database-route.db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"

    const val ACTION_PAUSE_OR_RESUME_SERVICE = "ACTION_PAUSE_OR_RESUME_SERVICE"

    const val ACTION_STOP_OR_RESUME_SERVICE = "ACTION_STOP_OR_RESUME_SERVICE"

    const val ACTION_SHOW_ROUTE_TRACKING_FRAGMENT = "ACTION_SHOW_ROUTE_TRACKING_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "route_channel"

    const val NOTIFICATION_CHANNEL_NAME = "Route Traking"

    const val NOTIFICATION_ID = 1

    const val POLYLINE_COLOR = Color.MAGENTA

    const val POLYLINE_WIDTH = 8f

    const val MAP_ZOOM = 20f

    const val TIME_UPDATE_INTERVAL = 50L

    const val LOCATION_UPDATE_INTERVAL = 5000L

    const val LOCATION_FASTEST_INTERVAL = 2000L
}