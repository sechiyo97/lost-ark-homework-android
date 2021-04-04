package com.queserasera.lostarkhomework.mari

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.mari.event.OnMariLoaded
import com.queserasera.lostarkhomework.mari.event.OnTabChanged
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.concurrent.thread

class MariViewModel : ViewModel() {
    val event: MutableLiveData<Event> = MutableLiveData()
    var mariItemSet: MariItemSet ?= null

    fun onReloadClicked(view: View) =
        loadMari()

    fun onTabClicked(index: Int) =
        setTab(index)

    fun loadMari() {
        thread {
            val t1t2List = mutableListOf<MariItem>()
            val t3List = mutableListOf<MariItem>()

            try {
                Jsoup.connect(MARI_URL_BASE).get()?.run{
                    select(MARI_T1_T2_BASE_SELECTOR)?.let{
                        t1t2List.storeMariItemListIn(it)
                    }
                    select(MARI_T3_BASE_SELECTOR)?.let{
                        t3List.storeMariItemListIn(it)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mariItemSet = MariItemSet(t1t2List, t3List)
            event.postValue(OnMariLoaded)
        }
    }

    fun setTab(index: Int) =
        event.postValue(OnTabChanged(index))

    private fun MutableList<MariItem>.storeMariItemListIn(elements: Elements) {
        val images = elements.select(MARI_IMAGE_SELECTOR)
        val names = elements.select(MARI_NAME_SELECTOR)
        val prices = elements.select(MARI_PRICE_SELECTOR)
        for (i in elements.indices) {
            this.add(
                MariItem(
                    names[i].text(),
                    prices[i].text().toInt(),
                    images[i].attributes()[SRC]
                )
            )
        }
    }

    companion object {
        const val SRC = "src"
        const val MARI_URL_BASE = "https://lostark.game.onstove.com/Shop/Mari"

        const val MARI_T1_T2_BASE_SELECTOR = "#lui-tab1-1 > ul > li > div"
        const val MARI_T3_BASE_SELECTOR = "#lui-tab1-2 > ul > li > div"

        const val MARI_IMAGE_SELECTOR = "div.thumbs > img"
        const val MARI_NAME_SELECTOR = "span.item-name"
        const val MARI_PRICE_SELECTOR = "div.area-amount > span"
    }
}