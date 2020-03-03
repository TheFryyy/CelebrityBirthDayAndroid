package com.example.celebrities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NavUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.purple)));

        setTitle(getIntent().getStringExtra("date"));

        ProfileListBundle profileListBundle = (ProfileListBundle) getIntent().getSerializableExtra("profiles");

        TextView numberOfResults = (TextView) findViewById(R.id.numberOfResults);
        numberOfResults.setText(profileListBundle.getProfiles().size()+" celebrities found");


        ProfilesAdapter adapter = new ProfilesAdapter(this, profileListBundle.getProfiles());
        ListView listview = (ListView)findViewById(R.id.list);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
