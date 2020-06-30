package com.queserasera.lostarkhomework;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MariActivity extends AppCompatActivity {

    public static final String sampleURL = "https://cdn-lostark.game.onstove.com/uploadfiles/tooltip/82fba916b31d449dbae5abe9c7f7fc86.png";
    private ProgressDialog progressDialog;

    private ImageView reloadButton;
    private String[] mariImageURL = new String[6];
    private ImageView[] mariImage = new ImageView[6];
    private TextView[] mariName = new TextView[6];
    private TextView[] mariPrice = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mari);

        mariImage[0] = (ImageView) findViewById(R.id.mari_image_0);
        mariImage[1] = (ImageView) findViewById(R.id.mari_image_1);
        mariImage[2] = (ImageView) findViewById(R.id.mari_image_2);
        mariImage[3] = (ImageView) findViewById(R.id.mari_image_3);
        mariImage[4] = (ImageView) findViewById(R.id.mari_image_4);
        mariImage[5] = (ImageView) findViewById(R.id.mari_image_5);

        mariName[0] = (TextView) findViewById(R.id.mari_name_0);
        mariName[1] = (TextView) findViewById(R.id.mari_name_1);
        mariName[2] = (TextView) findViewById(R.id.mari_name_2);
        mariName[3] = (TextView) findViewById(R.id.mari_name_3);
        mariName[4] = (TextView) findViewById(R.id.mari_name_4);
        mariName[5] = (TextView) findViewById(R.id.mari_name_5);

        mariPrice[0] = (TextView) findViewById(R.id.mari_price_0);
        mariPrice[1] = (TextView) findViewById(R.id.mari_price_1);
        mariPrice[2] = (TextView) findViewById(R.id.mari_price_2);
        mariPrice[3] = (TextView) findViewById(R.id.mari_price_3);
        mariPrice[4] = (TextView) findViewById(R.id.mari_price_4);
        mariPrice[5] = (TextView) findViewById(R.id.mari_price_5);

        reloadButton = (ImageView) findViewById(R.id.reload_button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadMari();
            }
        });
        loadMari();
    }

    // 웹에서 마리 정보 가져와 띄우기
    public void loadMari(){

        Thread parseThread = new Thread(){
            public void run(){
                String url = "https://lostark.game.onstove.com/Shop/Mari";
                String imageSelector = "#listItems > li > div > div.thumbs > img";
                String nameSelector = "#listItems > li > div > div.item-desc > span.item-name";
                String priceSelector = "#listItems > li > div > div.area-amount > span";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get(); // 1. get방식의 URL에 연결해서 가져온 값을 doc에 담는다.
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                Elements images = doc.select(imageSelector); // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
                Elements names = doc.select(nameSelector); // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
                Elements prices = doc.select(priceSelector); // 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.

                for(int i=0;i<names.size();i++) { // 3. Elemntes 길이만큼 반복한다.
                    mariName[i].setText(names.get(i).text()); // 4. 원하는 요소가 출력된다.
                    mariPrice[i].setText(prices.get(i).text());
                    mariImageURL[i] = "https:" + images.get(i).attributes().get("src");
                }
            }
        };
        parseThread.start();
        try{parseThread.join(); } catch(InterruptedException e){}

        // 이미지 로딩
        for(int i=0;i<6;i++) Glide.with(this).load(mariImageURL[i]).into(mariImage[i]);
    }
}
