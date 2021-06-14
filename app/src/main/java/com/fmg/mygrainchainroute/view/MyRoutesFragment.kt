package com.fmg.mygrainchainroute.view

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmg.mygrainchainroute.R
import com.fmg.mygrainchainroute.Results
import com.fmg.mygrainchainroute.databinding.FragmentMyRoutesBinding
import com.fmg.mygrainchainroute.repository.RouteRepository
import com.fmg.mygrainchainroute.source.MyGrainChainDatabase
import com.fmg.mygrainchainroute.view.adapters.RoutesAdapter
import com.fmg.mygrainchainroute.viewmodel.RouteViewModel

/**
 * A simple [Fragment] subclass.
 */
class MyRoutesFragment : Fragment() {

    private lateinit var binding : FragmentMyRoutesBinding
    private lateinit var navController: NavController
    private lateinit var routeViewModel: RouteViewModel
    private lateinit var adapter: RoutesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceViewModels()
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        routeViewModel.getRoutesInOrderByDate(requireContext())
        binding = FragmentMyRoutesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        adapter = this.requireContext().let {
            RoutesAdapter(it, navController,
                object : RoutesAdapter.OnClickListener {
                    override fun onItemClick(routeId: Int) {
                        var dataBundle = Bundle()
                        dataBundle.putInt("routeId", routeId)
                        navController.navigate(R.id.myRouteDetailFragment, dataBundle)
                    }
                })
        }
        binding.recyclerRoutes.adapter = adapter
        binding.recyclerRoutes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        setRoutesList()
    }

    private fun setRoutesList(){
        routeViewModel.mutableListRoutes.observe(viewLifecycleOwner, Observer {
            when(it){
                is Results.Success ->{
                    if (!it.data.isNullOrEmpty()) {
                        adapter.setListData(it.data.toMutableList())
                        adapter.notifyDataSetChanged()
                    }//end if
                }
                is Results.Error ->{
                    Toast.makeText(requireContext(), "No pudo obtener la Lista de Rutas",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
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
