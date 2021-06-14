package com.fmg.mygrainchainroute.view.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.Utilities
import com.fmg.mygrainchainroute.source.room.entities.Route
import kotlinx.android.synthetic.main.card_routes_item.view.*

class RoutesAdapter (private val context:Context,
                     private val navController: NavController,
                     val onClickListener: OnClickListener)
    :RecyclerView.Adapter<RoutesAdapter.ViewHolder>(){

    private var dataRouteList = mutableListOf<Route>()

    fun setListData(data: MutableList<Route>){
        dataRouteList = data
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(route: Route){

            itemView.iv_map.setImageBitmap(route.map)
            itemView.tv_route_name.text = route.nameRoute
            val distance = Utilities.convertMetersInKilometers(route.distanceInMeters)
            itemView.tv_distance.text = context.getString(R.string.distance) + ": " + distance+
                    " " +context.getString(R.string.kilometers)
            itemView.tv_date.text = context.getString(R.string.date) +
                    ": " + Utilities.convertTimeStampInDate(route.timeStamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(context).
                inflate(R.layout.card_routes_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataRouteList.size
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = dataRouteList[position]
        holder.bindView(route)

        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(route.routeId)
        }

    }

}