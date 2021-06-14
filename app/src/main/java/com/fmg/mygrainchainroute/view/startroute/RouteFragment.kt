package com.fmg.mygrainchainroute.view.startroute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fmg.mygrainchainroute.DataSingleton
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.databinding.FragmentRouteBinding

/**
 * A simple [Fragment] subclass.
 */
class RouteFragment : Fragment() {

    private lateinit var binding : FragmentRouteBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentRouteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.btnNavigate.setOnClickListener{
            if(validateRouteName()){
                navController.navigate(R.id.startRouteFragment)
            }
        }
    }

    private fun validateRouteName():Boolean{
        //Agregar ViewModel para Validacion de Lógica
        val routeName = binding.tilRouteName.editText!!.text.toString()

        return if (routeName.isNotEmpty()) {
            DataSingleton.routeName = routeName
            binding.tilRouteName.error = null
            true
        } else {
            binding.tilRouteName.error = getString(R.string.error_route_name)
            false
        }
    }

}
