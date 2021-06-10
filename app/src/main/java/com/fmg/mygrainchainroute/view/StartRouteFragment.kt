package com.fmg.mygrainchainroute.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fmg.mygrainchainroute.AbstractActivity
import com.fmg.mygrainchainroute.Constants
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.Utils
import com.fmg.mygrainchainroute.databinding.FragmentStartRouteBinding
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.services.Polyline
import com.fmg.mygrainchainroute.services.RouteTrackingService
import com.fmg.mygrainchainroute.source.MyGrainChainDatabase
import com.fmg.mygrainchainroute.view.customcontrols.ModalFragment
import com.fmg.mygrainchainroute.viewmodel.RouteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions

/**
 * A simple [Fragment] subclass.
 */
class StartRouteFragment : Fragment() {


    private lateinit var binding : FragmentStartRouteBinding
    private lateinit var navController: NavController
    private lateinit var routeViewModel: RouteViewModel
    private var isRouteStart = false
    private var pathPoints = mutableListOf<Polyline>()
    private var map: GoogleMap? = null
    private var currentTimeInMillis =0L
    private var utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceViewModels()
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentStartRouteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.btnActionRoute.setOnClickListener {
            actionRoute()
        }

        binding.mapView.getMapAsync{
            map = it
            addAllPolylines()
        }

        subscribeToObserver()
    }

    private fun sendCommandToService(action:String) =
        Intent(requireContext(), RouteTrackingService::class.java).also {
        it.action = action
            requireContext().startService(it)
    }

    private fun addLastestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size>1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size-2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Constants.POLYLINE_COLOR)
                .width(Constants.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun showDialogFinishRoute(){
        (requireActivity() as AbstractActivity).showDialog(false,
            true,
            "Finalizar Ruta",
            "¿Deseas Finalizar Ruta?",
            object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                override fun onCancel() {
                    actionRoute()
                    binding.btnActionRoute.text = getString(R.string.stop_route)
                }

                override fun onAccept() {
                    finishRoute()
                }
            })
    }

    private fun addAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(Constants.POLYLINE_COLOR)
                .width(Constants.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }//end for
    }

    private fun moveZoomToCurrentPosition(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }//end if
    }

    private fun updateRoute(isStart:Boolean){
        this.isRouteStart = isStart
        if(!isStart){
            binding.btnActionRoute.text = getString(R.string.start_route)
        }else{
            binding.btnActionRoute.text = getString(R.string.stop_route)
        }
    }

    private fun subscribeToObserver(){
        RouteTrackingService.isStartRoute.observe(viewLifecycleOwner, Observer {
            updateRoute(it)
        })

        RouteTrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLastestPolyline()
            moveZoomToCurrentPosition()
        })

        RouteTrackingService.timeInRouteInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = utils.getFormattedStopWatchTime(currentTimeInMillis, true)
            binding.tvTimer.text = formattedTime
        })

    }

    private fun actionRoute(){
        if(isRouteStart){
            sendCommandToService(Constants.ACTION_PAUSE_OR_RESUME_SERVICE)
            showDialogFinishRoute()
        }else{
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun finishRoute(){
        sendCommandToService(Constants.ACTION_STOP_OR_RESUME_SERVICE)
        //Send to Set Data Route
        navController.navigate(R.id.myRouteFragment)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun instanceViewModels(){
        routeViewModel = ViewModelProvider(this,
            RouteViewModel.FACTORY(RouteRepository(
                MyGrainChainDatabase.getInstance(requireContext()).routeDao())))
            .get(RouteViewModel::class.java)
    }

}
