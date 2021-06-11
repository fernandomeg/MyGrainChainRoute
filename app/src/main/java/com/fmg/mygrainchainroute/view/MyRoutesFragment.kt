package com.fmg.mygrainchainroute.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fmg.mygrainchainroute.DataSingleton
import com.fmg.mygrainchainroute.databinding.FragmentMyRoutesBinding
import com.fmg.mygrainchainroute.source.room.entities.Route
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MyRoutesFragment : Fragment() {


    private lateinit var binding : FragmentMyRoutesBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentMyRoutesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        if(DataSingleton.route!=null) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = DataSingleton.route.timeStamp
            }

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            binding.tvRoute.text =
                "Distancia:  " + DataSingleton.route.distanceInMeters + "metros,  Fecha: " + dateFormat.format(
                    calendar.time
                )
            binding.ivMap.setImageBitmap(DataSingleton.route.map)
        }
    }

}
