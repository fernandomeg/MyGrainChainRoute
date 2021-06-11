package com.fmg.mygrainchainroute

import com.fmg.mygrainchainroute.source.room.entities.Route


object DataSingleton {

    var routeName: String = ""
        get() = field                     // getter
        set(value) { field = value }      // setter

    var route: Route = Route()
        get() = field                     // getter
        set(value) { field = value }

}