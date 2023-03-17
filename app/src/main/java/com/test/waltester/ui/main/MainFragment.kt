package com.test.waltester.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.waltester.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), DefaultLifecycleObserver {
    private lateinit var viewModel: WTCountriesViewModel
    private lateinit var recyclerAdapter: WTCountriesRecyclerAdapter

    private var binding: FragmentMainBinding? = null

    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
        activity?.lifecycle?.removeObserver(this)
        viewModel = activity?.run {
            ViewModelProvider(this)[WTCountriesViewModel::class.java]
        }?: throw Exception("No Activity!")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView()")
        binding = FragmentMainBinding.inflate(inflater)
        // a basic RC setup with a LinearLayoutManager
        binding?.wtrecyclerview?.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
            recyclerAdapter = WTCountriesRecyclerAdapter()
            adapter = recyclerAdapter
        }
        // observe the latest countries data and send to the recycler adapter
        // diffUtil (no duplicate items).
        viewModel.countries.observe(viewLifecycleOwner){ countyInfoList ->
            recyclerAdapter.differ.submitList(countyInfoList)
        }
        // only loading data once: further requirements needed.
        if(recyclerAdapter.differ.currentList.isEmpty()) {
            viewModel.refreshTitle()
        }
        return binding?.root
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }
}