package com.queserasera.lostarkhomework.mari

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event

class MariVIewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()

}