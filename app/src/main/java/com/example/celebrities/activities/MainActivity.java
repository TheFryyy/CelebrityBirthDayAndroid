package com.example.celebrities.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.celebrities.model.Profile;
import com.example.celebrities.utils.Accelerometer;
import com.example.celebrities.utils.ProfileListBundle;
import com.example.celebrities.R;
import com.squareup.seismic.ShakeDetector;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShakeDetector.Listener{
    private DatePicker datePicker;
    private SensorManager sensorMgr;
    private String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* *****  Initialize sensor manager (seismic library) to detect shakes ***** */
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.purple)));

        datePicker = findViewById(R.id.date_picker);

        /* *****  Default initialization  ***** */
        datePicker.init(1998, 7, 17, null);


        /* *****  Months array for url research  ***** */
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
            searchCelebrities();
        }
    }

    public void searchCelebrities()
    {

        RequestQueue queue = Volley.newRequestQueue(this);

        /* *****  create the url corresponding to the search   ***** */
        String url ="https://www.thefamousbirthdays.com/"+months[datePicker.getMonth()]+"-"+datePicker.getDayOfMonth();

        /* *****  Get layout objects  ***** */

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        /* *****  Show progress bar  ***** */
        progressBar.setVisibility(View.VISIBLE);

        searchButton.setText("Searching ...");
        searchButton.setEnabled(false);

        /* *****  Create Volley get request to retrieve html code ***** */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /* *****  Hide progress bar ***** */
                        progressBar.setVisibility(View.INVISIBLE);
                        searchButton.setText("Search");

                        List<Profile> profiles = new ArrayList<>();

                        /* *****  HTML parsing ***** */
                        Document doc = Jsoup.parse(response);
                        Elements elements = doc.select(".item");
                        for (Element element : elements) {
                            Profile profile = new Profile();
                            profile.setTitle(element.select(".details a").text());
                            profile.setDetailPageURL(element.select(".details a").attr("href"));
                            profile.setProfession(element.select(".details p").text());
                            profile.setImageURL(element.select(".thumb img").attr("src"));
                            profiles.add(profile);
                        }

                        /* *****  opening a new activity with date and profiles list extras ***** */
                        Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                        i.putExtra("date",months[datePicker.getMonth()]+" "+datePicker.getDayOfMonth());
                        i.putExtra("profiles", new ProfileListBundle(profiles)); //serializable object to pass the list between activities
                        startActivity(i);

                        searchButton.setEnabled(true);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /* *****  Show a toast if an error occured ***** */
                Toast.makeText(getApplicationContext(),"Didn't work",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void hearShake() {
        /* *****  Automatic search if a shake is detected ***** */
        searchCelebrities();
    }
}
