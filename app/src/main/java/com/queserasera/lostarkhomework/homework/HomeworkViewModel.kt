package com.queserasera.lostarkhomework.homework

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.common.GameCharacter
import com.queserasera.lostarkhomework.homework.event.OnResetDailyClicked
import com.queserasera.lostarkhomework.homework.event.OnResetWeeklyClicked
import com.queserasera.lostarkhomework.main.event.OnHomeworkLoaded
import com.queserasera.lostarkhomework.mari.event.OnTabChanged
import kotlin.concurrent.thread

class HomeworkViewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()

    // TODO: DB에서 받아오기
    fun loadHomework(character: GameCharacter) {
        thread {
            event.postValue(OnHomeworkLoaded)
        }
    }

    fun resetDailyHomework(character: GameCharacter) {
        // TODO: clear
        loadHomework(character)
    }

    fun resetWeeklyHomework(character: GameCharacter) {
        // TODO: clear
        loadHomework(character)
    }

    fun onResetDailyClicked(view: View) =
        event.postValue(OnResetDailyClicked)

    fun onResetWeeklyClicked(view: View) =
        event.postValue(OnResetWeeklyClicked)

    fun onTabClicked(index: Int) =
        setTab(index)

    fun setTab(index: Int) =
        event.postValue(OnTabChanged(index))
}