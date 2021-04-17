package com.queserasera.lostarkhomework.mari

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.databinding.ActivityMariBinding
import com.queserasera.lostarkhomework.mari.event.OnMariLoaded
import com.queserasera.lostarkhomework.mari.event.OnTabChanged

class MariActivity : AppCompatActivity() {
    private var binding: ActivityMariBinding? = null
    private val viewModel = MariViewModel()
    private val mariAdapter = MariAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mari)
        binding?.viewModel = viewModel

        binding?.arkMariList?.apply {
            adapter = mariAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.event.observe(this, Observer<Event>{
            when(it) {
                OnMariLoaded -> {
                    mariAdapter.mariSet = viewModel.mariItemSet
                    mariAdapter.notifyDataSetChanged()
                }
                is OnTabChanged -> {
                    if (binding?.selectedTab != it.index) {
                        binding?.selectedTab = it.index
                        mariAdapter.selectedTab = it.index
                        mariAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

        viewModel.loadMari()
        viewModel.setTab(0)
    }
}