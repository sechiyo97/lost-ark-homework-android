package com.queserasera.lostarkhomework

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class MariActivity : AppCompatActivity() {
    private val progressDialog: ProgressDialog? = null
    private var reloadButton: ImageView? = null
    private val mariImageURL: Array<String?> = arrayOfNulls(6)
    private val mariImage: Array<ImageView?> = arrayOfNulls(6)
    private val mariName: Array<TextView?> = arrayOfNulls(6)
    private val mariPrice: Array<TextView?> = arrayOfNulls(6)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mari)
        mariImage[0] = findViewById(R.id.mari_image_0)
        mariImage[1] = findViewById(R.id.mari_image_1)
        mariImage[2] = findViewById(R.id.mari_image_2)
        mariImage[3] = findViewById(R.id.mari_image_3)
        mariImage[4] = findViewById(R.id.mari_image_4)
        mariImage[5] = findViewById(R.id.mari_image_5)
        mariName[0] = findViewById(R.id.mari_name_0)
        mariName[1] = findViewById(R.id.mari_name_1)
        mariName[2] = findViewById(R.id.mari_name_2)
        mariName[3] = findViewById(R.id.mari_name_3)
        mariName[4] = findViewById(R.id.mari_name_4)
        mariName[5] = findViewById(R.id.mari_name_5)
        mariPrice[0] = findViewById(R.id.mari_price_0)
        mariPrice[1] = findViewById(R.id.mari_price_1)
        mariPrice[2] = findViewById(R.id.mari_price_2)
        mariPrice[3] = findViewById(R.id.mari_price_3)
        mariPrice[4] = findViewById(R.id.mari_price_4)
        mariPrice[5] = findViewById(R.id.mari_price_5)
        reloadButton = findViewById(R.id.reload_button)
        reloadButton!!.setOnClickListener { loadMari() }
        loadMari()
    }

    // 웹에서 마리 정보 가져와 띄우기
    private fun loadMari() {
        val parseThread: Thread = object : Thread() {
            override fun run() {
                val url = "https://lostark.game.onstove.com/Shop/Mari"
                val imageSelector = "#listItems > li > div > div.thumbs > img"
                val nameSelector = "#listItems > li > div > div.item-desc > span.item-name"
                val priceSelector = "#listItems > li > div > div.area-amount > span"
                var doc: Document? = null
                try {
                    doc = Jsoup.connect(url).get() // 1. get방식의 URL에 연결해서 가져온 값을 doc에 담는다.
                } catch (e: IOException) {
                    println(e.message)
                }
                val images = doc!!.select(imageSelector) // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
                val names = doc.select(nameSelector) // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
                val prices = doc.select(priceSelector) // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
                for (i in names.indices) { // 3. Elemntes 길이만큼 반복한다.
                    mariName[i]?.text = names[i].text() // 4. 원하는 요소가 출력된다.
                    mariPrice[i]?.text = prices[i].text()
                    mariImageURL[i] = "https:" + images[i].attributes()["src"]
                }
            }
        }
        parseThread.start()
        try {
            parseThread.join()
        } catch (e: InterruptedException) {
        }

        // 이미지 로딩
        for (i in 0..5) mariImage[i]?.let { Glide.with(this).load(mariImageURL[i]).into(it) }
    }
}