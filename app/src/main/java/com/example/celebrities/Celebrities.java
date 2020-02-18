package com.example.celebrities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Celebrities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrities);

        LinearLayout home = findViewById(R.id.bg_list_celebrities);
        home.setBackgroundColor(Color.rgb(255,98,1));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
