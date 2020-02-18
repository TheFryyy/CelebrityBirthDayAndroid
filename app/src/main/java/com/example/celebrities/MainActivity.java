package com.example.celebrities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout home = findViewById(R.id.bg_main);
        home.setBackgroundColor(Color.rgb(255,98,1)); // set the background color to orange

        datePicker = findViewById(R.id.date_picker); // recover the DatePicker with it id

        /* *****  Default initiation  ***** */
        datePicker.init(1998, 7, 17, null);

        Button button = findViewById(R.id.search_celebrities);

        button.setOnClickListener(new View.OnClickListener() { // Define the function called when clicking the search button
            @Override
            public void onClick(View v) {
                Intent celebrities = new Intent(MainActivity.this, Celebrities.class);
                startActivity(celebrities); // launch the activity Celebrities
            }
        });
    }
    protected void onResume()
    {
        super.onResume();
        /* *****  Search for  the saved date and update the DatePicker when resuming the activity  ***** */
        int year = loadDate()[0];
        int month = loadDate()[1];
        int day = loadDate()[2];
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                saveDate(year, month, dayOfMonth); // save the data when the date is changed
            }
        });


    }

    protected void saveDate(int year, int month, int dayOfMonth){ // function which save the date using shared preferences to make the date persistent
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", dayOfMonth);
        editor.apply();
    }

    protected int[] loadDate(){ // function retrieving the date using shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        int[] date = {sharedPreferences.getInt("year", 1998), sharedPreferences.getInt("month", 07), sharedPreferences.getInt("day", 17)};
        return date;
    }
}
