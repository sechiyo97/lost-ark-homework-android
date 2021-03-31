package com.queserasera.lostarkhomework.homework

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event

class HomeworkViewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()

}