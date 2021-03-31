package com.queserasera.lostarkhomework.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.main.event.OnHomeworkClicked
import com.queserasera.lostarkhomework.main.event.OnMariClicked

class MainViewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()

    fun onMariButtonClicked(view: View) =
        event.postValue(OnMariClicked)

    fun onHomeworkButtonClicked(view: View) =
        event.postValue(OnHomeworkClicked)
}