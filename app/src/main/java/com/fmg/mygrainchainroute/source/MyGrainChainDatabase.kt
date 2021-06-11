package com.fmg.mygrainchainroute.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fmg.mygrainchainroute.Converters
import com.fmg.mygrainchainroute.source.room.dao.RouteDao
import com.fmg.mygrainchainroute.source.room.entities.Route

@Database(entities = [Route::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyGrainChainDatabase: RoomDatabase() {

    abstract fun routeDao(): RouteDao

    companion object{
        private var INSTANCE : MyGrainChainDatabase? = null

        //Se agregó el allowMainThreadQueries para que nos deje operar la DB en el hilo principal

        //Se agregó fallbackToDestructiveMigration para que cuando se actualice la versión de DB, elimine la DB y cree una nueva

        fun getInstance(context: Context): MyGrainChainDatabase {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MyGrainChainDatabase::class.java,"database-route"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!
        }
    }

}