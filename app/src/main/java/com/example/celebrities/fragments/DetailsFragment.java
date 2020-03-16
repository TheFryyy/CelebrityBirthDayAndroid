package com.example.celebrities.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.celebrities.R;
import com.example.celebrities.activities.MainActivity;
import com.example.celebrities.activities.SearchResultsActivity;
import com.example.celebrities.model.Profile;
import com.example.celebrities.utils.CustomVolleyRequest;
import com.example.celebrities.utils.ProfileListBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DetailsFragment extends Fragment {

    private Profile profile;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (Profile) getArguments().getSerializable("profile");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_celebrity_details, container, false);
        if(profile!=null)
            loadDetails(view);
        return view;
    }

    public void loadDetails(View view) {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressBar progressBar = view.findViewById(R.id.detailsProgressBar);
        final TextView celebrity_name = view.findViewById(R.id.celebrity_name);
        final NetworkImageView imageView = view.findViewById(R.id.celebrity_picture);
        final TextView profession = view.findViewById(R.id.celebrity_profession);
        final TextView birthday = view.findViewById(R.id.celebrity_birthday);
        final TextView age = view.findViewById(R.id.celebrity_age);
        final TextView birthplace = view.findViewById(R.id.celebrity_birthplace);
        final TextView aboutTitle = view.findViewById(R.id.celebrity_aboutTitle);
        final TextView description = view.findViewById(R.id.celebrity_description);
        progressBar.setVisibility(View.VISIBLE);

        String url = profile.getDetailPageURL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /* Parsing html and setting values in profile attributes */
                        Document doc = Jsoup.parse(response);
                        profile.setName(doc.select(".entry-title").select("a").first().text());
                        profile.setBirthday(doc.select("time").attr("datetime"));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = new Date();
                        Date birthdayDate = null;
                        try {
                            birthdayDate = formatter.parse(profile.getBirthday().toString());
                            long diffInMillies = Math.abs(currentDate.getTime() - birthdayDate.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            profile.setAge((int) Math.floor(diff/365));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        profile.setDescription(doc.select("#profile_about_description").text());
                        profile.setBirthPlace(doc.select("span[itemprop = birthPlace]").text());
                        progressBar.setVisibility(View.INVISIBLE);

                        /* Setting layouts components */
                        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getContext())
                                .getImageLoader();

                        imageLoader.get(profile.getImageURL(), ImageLoader.getImageListener(imageView,android.R.drawable.stat_sys_download ,android.R.drawable.ic_dialog_alert));
                        imageView.setImageUrl(profile.getImageURL(), imageLoader);

                        celebrity_name.setText(profile.getName());
                        profession.setText(profile.getProfession());
                        birthday.setText(profile.getBirthday());
                        age.setText(profile.getAge()+" years old");
                        birthplace.setText(profile.getBirthPlace());
                        aboutTitle.setText("About "+profile.getName());
                        description.setText(profile.getDescription());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Details were not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
