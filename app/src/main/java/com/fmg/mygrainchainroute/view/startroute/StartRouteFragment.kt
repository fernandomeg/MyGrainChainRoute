package com.fmg.mygrainchainroute.view.startroute

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fmg.mygrainchainroute.*
import com.fmg.mygrainchainroute.databinding.FragmentStartRouteBinding
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.services.Polyline
import com.fmg.mygrainchainroute.services.RouteTrackingService
import com.fmg.mygrainchainroute.source.MyGrainChainDatabase
import com.fmg.mygrainchainroute.source.room.entities.Route
import com.fmg.mygrainchainroute.view.customcontrols.ModalFragment
import com.fmg.mygrainchainroute.viewmodel.RouteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*

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

    private var isCurrentLocation: Boolean=true
    private var isFinishLocation: Boolean=false
    private lateinit var marketInitial :Marker
    private lateinit var markerFinal :Marker

    private var routeName :String? = null

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
        zoomToGetRoute()
        (requireActivity() as AbstractActivity).showDialog(false,
            true,
            "Finalizar Ruta",
            "¿Deseas Finalizar Ruta?",
            object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                override fun onCancel() {
                    actionRoute()
                    binding.btnActionRoute.text = getString(R.string.stop_route)
                    markerFinal.remove()
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

    private fun addCurrentMarker(){
        if(isCurrentLocation){
            isCurrentLocation = false
            marketInitial = map!!.addMarker(MarkerOptions().position(pathPoints.last().last()).title("Ubicación Actual"))
        }
    }

    private fun addFinishLocationMarker(marker:Boolean){
        if(marker){
            markerFinal = map!!.addMarker(MarkerOptions().position(pathPoints.last().last()).title("Ubicación Final"))
        }
    }

    private fun moveZoomToCurrentPosition(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            addCurrentMarker()
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }//end if
    }

    private fun zoomToGetRoute(){
        val bounds = LatLngBounds.builder()
        for(polyline in pathPoints){
            for(position in polyline){
                bounds.include(position)
            }
        }

        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(
            bounds.build(),
            binding.mapView.width,
            binding.mapView.height,
            (binding.mapView.height*0.05f).toInt()
        )
        )
    }

    private fun finishRouteAndSaveData(){
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints){
                distanceInMeters += Utilities.calculatePolyline(polyline).toInt()
            }

            val dateTimeStmp =Calendar.getInstance().timeInMillis
            val route = Route()

            if(bmp!=null){
                route.map = bmp
            }
            route.nameRoute = DataSingleton.routeName
            route.timeStamp = dateTimeStmp
            route.distanceInMeters = distanceInMeters
            route.timeInMillis = currentTimeInMillis
            routeViewModel.insertRoute(route)
        }
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
            val formattedTime = Utilities.getFormattedStopWatchTime(currentTimeInMillis, true)
            binding.tvTimer.text = formattedTime
        })

    }

    private fun actionRoute(){
        if(isRouteStart){
            sendCommandToService(Constants.ACTION_PAUSE_OR_RESUME_SERVICE)
            addFinishLocationMarker(true)
            showDialogFinishRoute()
        }else{
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun finishRoute(){
        finishRouteAndSaveData()
        addFinishLocationMarker(true)
        sendCommandToService(Constants.ACTION_STOP_OR_RESUME_SERVICE)
        //Send to Set Data Route
        (requireActivity() as AbstractActivity).showDialog(false,false,"Ruta Guardada",
            "La Ruta ha Sido Guardada Exitosamente", object : ModalFragment.CommonDialogFragmentCallBackWithCancel {
                override fun onCancel() {/*No implementation*/}

                override fun onAccept() {
                    navController.navigate(R.id.myRouteFragment)
                }
            })
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
