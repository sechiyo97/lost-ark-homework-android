package com.queserasera.lostarkhomework.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.main.event.*
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()
    var characterList : MutableList<Character> = mutableListOf()

    init {
        loadCharacterList()
    }

    fun onArrowUpClicked(view: View) =
        event.postValue(OnArrowUpClicked)


    fun onArrowDownClicked(view: View) =
        event.postValue(OnArrowDownClicked)

    fun onMariButtonClicked(view: View) =
        event.postValue(OnMariClicked)

    fun onHomeworkButtonClicked(view: View) =
        event.postValue(OnHomeworkClicked)

    fun loadCharacterList() {
        thread {
            val foundList = mutableListOf<Character>()

            try {
                Jsoup.connect(CHARACTER_URL_BASE).get()?.run{
                    select(CHARACTER_LIST_SELECTOR)?.let{
                        it.forEach { character ->
                            println(character.text())
                            println("hmmm ${character.text().split(SPACE)}")
                            foundList.add(
                                Character(
                                    character.text().split(SPACE)[1],
                                    character.text().split(SPACE)[0].split(DOT)[1].toInt()
                                )
                            )
                        }
                    }
                }
                characterList = foundList
                event.postValue(OnCharactersLoaded)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val SPACE = " "
        const val DOT = "."
        const val CHARACTER_URL_BASE = "https://m-lostark.game.onstove.com/Profile/Character/%EC%95%84%EB%A5%B4%EC%9C%BC%EC%9E%89"
        const val CHARACTER_LIST_SELECTOR = "#myinfo__character--list2 > div > ul > li"
    }
}