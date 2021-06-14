package com.fmg.mygrainchainroute.repository

import android.content.Context
import android.util.Log
import com.fmg.mygrainchainroute.Results
import com.fmg.mygrainchainroute.source.MyGrainChainDatabase
import com.fmg.mygrainchainroute.source.room.dao.RouteDao
import com.fmg.mygrainchainroute.source.room.entities.Route
import kotlinx.coroutines.CompletableDeferred

class RouteRepository constructor(private val routeDao:RouteDao) {

    suspend fun insertRoute(route:Route) = routeDao.insertRoute(route)

    suspend fun updateRoute(route: Route) = routeDao.updateRoute(route)

    suspend fun deleteRoute(route:Route) = routeDao.deleteRoute(route)

    suspend fun getRoutesOrderByDate(context: Context):Results<List<Route>>{

        var listRoutes = CompletableDeferred<Results<List<Route>>>()

        try{
            var listRoutesDataResult = Results.Success(MyGrainChainDatabase.getInstance(context).routeDao().getRoutesListByDate())
            listRoutes.complete(listRoutesDataResult)
        }catch (exe:Exception){
            Log.e(RouteRepository::class.java.toString(), exe.message.toString())
            throw exe
        }

        return listRoutes.await()
    }

    suspend fun getRouteById(context: Context,routeId:Int):Results<Route>{

        var route = CompletableDeferred<Results<Route>>()

        try{
            var routeDataResult = Results.Success(MyGrainChainDatabase.getInstance(context).routeDao().getRouteById(routeId))
            route.complete(routeDataResult)
        }catch (exe:Exception){
            Log.e(RouteRepository::class.java.toString(), exe.message.toString())
            throw exe
        }

        return route.await()
    }

}