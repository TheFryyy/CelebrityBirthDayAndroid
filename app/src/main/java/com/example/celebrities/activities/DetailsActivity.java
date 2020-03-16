package com.example.celebrities.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.celebrities.R;
import com.example.celebrities.fragments.CelebrityListFragment;
import com.example.celebrities.fragments.DetailsFragment;
import com.example.celebrities.model.Profile;

public class DetailsActivity extends AppCompatActivity{

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        profile = (Profile) getIntent().getSerializableExtra("profile");
        setTitle(profile.getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.purple)));

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("profile",profile);
        detailsFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,detailsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubject = "which celebrity was born on the same day as me ?";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm born the same day as "+profile.getTitle());
                startActivity(Intent.createChooser(shareIntent, "share using"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //add the share button to the action bar
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }
}
