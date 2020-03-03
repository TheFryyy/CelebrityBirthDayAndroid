package com.example.celebrities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DatePicker datePicker;

    private String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.purple)));

        datePicker = findViewById(R.id.date_picker); // recover the DatePicker with it id

        /* *****  Default initiation  ***** */
        datePicker.init(1998, 7, 17, null);

        months = new String[] {"january","february","march","april","may","june","july","august","september","october","november","december"};
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

    public void onClick(View view) {
        if (view.getId() == R.id.searchButtonAndProgress){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://www.thefamousbirthdays.com/"+months[datePicker.getMonth()]+"-"+datePicker.getDayOfMonth();
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            final Button searchButton = (Button) findViewById(R.id.searchButton);

            progressBar.setVisibility(View.VISIBLE);
            searchButton.setText("Searching ...");
            searchButton.setEnabled(false);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            searchButton.setText("Search");

                            List<Profile> profiles = new ArrayList<>();

                            Document doc = Jsoup.parse(response);
                            Elements elements = doc.select(".item");
                            for (Element element : elements) {
                                Profile profile = new Profile();
                                profile.setTitle(element.select(".details a").text());
                                profile.setProfession(element.select(".details p").text());
                                profile.setImageURL(element.select(".thumb img").attr("src"));
                                profiles.add(profile);
                            }

                            Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                            i.putExtra("date",months[datePicker.getMonth()]+" "+datePicker.getDayOfMonth());
                            i.putExtra("profiles", new ProfileListBundle(profiles));
                            startActivity(i);

                            searchButton.setEnabled(true);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Didn't work",Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}
