package com.example.celebrities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Details extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /* **********  Retrieve the profile of the selected celebrity  *********  */
        profile = (Profile) getIntent().getSerializableExtra("profile");

        /* **********  take the age from the title (Jean Dujardin (50) => 50)  *********  */
        getCelebrityAge();

        /* **********  Retrieve other information needed form the API and set it in the xml file  *********  */
        searchCelebrities();

        /* **********  Initialisation of UI elements  *********  */
        title = findViewById(R.id.celebrity_title);
        description = findViewById(R.id.celebrity_description);
        TextView name = findViewById(R.id.celebrity_name);
        name.setText(getCelebrityNameURL().split("-")[0] + " " + getCelebrityNameURL().split("-")[1]);
        ((TextView) findViewById(R.id.celebrity_profession)).setText(profile.getProfession());
        ((TextView) findViewById(R.id.celebrity_age)).setText(profile.getAge() + " years old");

        /* **********  Retrieve the picture of the celebrity from url and set it in the xml file  *********  */
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(this)
                .getImageLoader();
        NetworkImageView imageView = findViewById(R.id.celebrity_picture);
        imageLoader.get(profile.getImageURL(), ImageLoader.getImageListener(imageView, android.R.drawable.stat_sys_download, android.R.drawable.ic_dialog_alert));
        imageView.setImageUrl(profile.getImageURL(), imageLoader);

    }

    /* **********  This function parse the html file to recover needed informations  *********  */
    public void searchCelebrities() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.thefamousbirthdays.com/people/" + getCelebrityNameURL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = Jsoup.parse(response);
                        Elements elements = doc.select("#profile_about_description");
                        profile.setBirthday(doc.select(".innerwrap time").text());
                        ((TextView) findViewById(R.id.celebrity_birthday)).setText(profile.getBirthday());
                        Element span = doc.select("#demo-data span").first();
                        profile.setBirthPlace(span.text());
                        ((TextView) findViewById(R.id.celebrity_birthplace)).setText(profile.getBirthPlace());
                        title.setText(elements.select(".section-title").text());
                        if(elements.select(".section-title").text()!="")
                        {
                            description.setText(elements.select("p").text());
                        }
                        else
                        {
                            description.setText(doc.select("#profile_rank_description p").text());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Didn't work", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public String getCelebrityNameURL() {
        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        String[] celebrityNameArray = profile.getTitle().split("\\(")[0].split(" ");
        String celebrityName = "";
        if (celebrityNameArray.length > 0) {
            for (int i = 0; i < celebrityNameArray.length - 1; i++) {
                celebrityName += celebrityNameArray[i];
                if (i != celebrityNameArray.length - 1)
                    celebrityName += "-";
            }
            celebrityName += celebrityNameArray[celebrityNameArray.length - 1];
        }
        return celebrityName;
    }

    public void getCelebrityAge() {
        String[] celebrityNameArray = profile.getTitle().split(" ");

        String celebrityName = celebrityNameArray[celebrityNameArray.length - 1];
        celebrityName = celebrityName.substring(0, celebrityName.length() - 1);
        celebrityName = celebrityName.substring(1, celebrityName.length());
        profile.setAge(Integer.parseInt(celebrityName));
    }
}