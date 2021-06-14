package com.fmg.mygrainchainroute.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fmg.mygrainchainroute.AbstractActivity
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.Results
import com.fmg.mygrainchainroute.Utilities
import com.fmg.mygrainchainroute.databinding.FragmentMyRouteDetailBinding
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.source.MyGrainChainDatabase
import com.fmg.mygrainchainroute.source.room.entities.Route
import com.fmg.mygrainchainroute.view.customcontrols.ModalFragment
import com.fmg.mygrainchainroute.viewmodel.RouteViewModel

/**
 * A simple [Fragment] subclass.
 */
class MyRouteDetailFragment : Fragment() {

    private lateinit var binding : FragmentMyRouteDetailBinding
    private lateinit var navController: NavController
    private lateinit var routeViewModel: RouteViewModel
    private var routeId:Int=0
    private lateinit var route: Route

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceViewModels()
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigate(R.id.myRouteFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        if(requireArguments().containsKey("routeId")){
            routeId = requireArguments().getInt("routeId")
        }

        binding = FragmentMyRouteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        routeViewModel.getRoutesById(requireContext(), routeId)
        binding.ivBack.setOnClickListener{
            navController.navigate(R.id.myRouteFragment)
        }

        binding.ivShare.setOnClickListener{
           shareData()
        }

        binding.btnDeleteRoute.setOnClickListener{
            (requireActivity() as AbstractActivity).showDialog(true,
                true,getString(R.string.title_delete), getString(R.string.delete_route_confirmation),
                object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                override fun onCancel() {/*No implementation*/}

                override fun onAccept() {
                    routeViewModel.deleteRoute(route)
                    navController.navigate(R.id.myRouteFragment)
                }
            })//end Dialog
        }

        getRouteDetail()
    }

    private fun getRouteDetail(){
            routeViewModel.mutableRoute.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Results.Success -> {
                        if (it.data != null) {
                            route = it.data
                            bindData()
                        }//end if
                    }
                    is Results.Error -> {
                        Toast.makeText(
                            requireContext(), "No pudo obtener el Detalle de Ruta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun bindData() {
        binding.ivMap.setImageBitmap(route.map)
        binding.tvRouteName.text = route.nameRoute
        binding.tvRouteDistance.text = getString(R.string.distance) + ": " +
                Utilities.convertMetersInKilometers(route.distanceInMeters) +
                " " + getString(R.string.kilometers)
        binding.tvRouteTime.text = getString(R.string.time) + ": "+
            Utilities.getFormattedStopWatchTime(route.timeInMillis)
    }

    private fun shareData(){
        var shareIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, getString(R.string.route_name_title) + ": " +
                    route.nameRoute +"\n" + "\n" +
                    getString(R.string.distance) + ": " +
                    Utilities.convertMetersInKilometers(route.distanceInMeters) +
                    " " + getString(R.string.kilometers) + "\n" +
                    getString(R.string.time) + ": "+
                    Utilities.getFormattedStopWatchTime(route.timeInMillis) +"\n" +"\n" +
                    getString(R.string.date) + ": " +
                    Utilities.convertTimeStampInDate(route.timeStamp)
            )
            this.type = "text/plain"
        }
        startActivity(shareIntent)
    }

    private fun instanceViewModels(){
        routeViewModel = ViewModelProvider(this,
            RouteViewModel.FACTORY(
                RouteRepository(
                    MyGrainChainDatabase.getInstance(requireContext()).routeDao())
            ))
            .get(RouteViewModel::class.java)
    }

}
