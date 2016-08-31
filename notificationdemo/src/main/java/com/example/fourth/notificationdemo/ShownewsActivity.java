package com.example.fourth.notificationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class ShownewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownews);


        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        TextView tv = (TextView) findViewById(R.id.tv_notify2News_showNews);
        if (url!=null){
            tv.setText(url);
        }
    }
}
