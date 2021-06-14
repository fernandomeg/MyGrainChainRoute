package com.fmg.mygrainchainroute.source.room.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.fmg.mygrainchainroute.source.room.entities.Route

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoute(route: Route)

    @Update
    fun updateRoute(route: Route)

    @Delete
    fun deleteRoute(route: Route)

    @Query("SELECT * FROM route")
    fun getRouteList(): List<Route>

    @Query("SELECT * FROM route ORDER BY timeStamp DESC")
    fun getRoutesListByDate(): List<Route>

    @Query("SELECT * FROM route WHERE routeId = :routeId")
    fun getRouteById(routeId:Int): Route
}