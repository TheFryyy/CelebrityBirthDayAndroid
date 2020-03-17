package com.example.celebrities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.celebrities.model.Profile;
import com.example.celebrities.utils.ProfileListBundle;
import com.example.celebrities.utils.ProfilesAdapter;
import com.example.celebrities.R;
import com.example.celebrities.fragments.DetailsFragment;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        /* *****  Modify action bar ***** */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.purple)));

        setTitle("Results for "+getIntent().getStringExtra("date"));

        /* *****  retrieve profiles list ***** */
        final ProfileListBundle profileListBundle = (ProfileListBundle) getIntent().getSerializableExtra("profiles");


        /* *****  Fill list view thanks to the custom adapter ***** */
        ProfilesAdapter adapter = new ProfilesAdapter(this, profileListBundle.getProfiles());
        ListView listview = (ListView)findViewById(R.id.list);
        listview.setAdapter(adapter);

        /* *****  Click event on a list element ***** */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                    /* *****  If device is vertical, open a new activity with only details fragment ***** */
                    Intent detailIntent = new Intent(SearchResultsActivity.this, DetailsActivity.class);
                    detailIntent.putExtra("profile",profileListBundle.getProfiles().get(position));
                    startActivity(detailIntent);

                }else{
                    /* *****  If device is horizontal, layout is automatically changed and fragment is dynamically created at the right of the list ***** */
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("profile",profileListBundle.getProfiles().get(position));
                    detailsFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,detailsFragment).commit();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /* *****  Go to previous view in the stack if back button is clicked  ***** */
                super.onBackPressed();
                return true;
            case R.id.action_share:

                /* *****  Show share options if share is clicked  ***** */
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubject = "which celebrities were born on the same day as you ?";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getBodyMessage());
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


    /* **********  return the message which will be shared  *********  */
    public String getBodyMessage()
    {
        List<Profile> profiles;
        ProfileListBundle profileListBundle = (ProfileListBundle) getIntent().getSerializableExtra("profiles");
        profiles = profileListBundle.getProfiles();
        String shareBody = "I am born the same day as: ";
        for(int i=0;i<2;i++)//Profile p : profiles)
        {
            shareBody += profiles.get(i).getTitle().split("\\(")[0];
            shareBody += ", ";
        }
        shareBody+=profiles.get(2).getTitle().split("\\(")[0];
        shareBody+= " and a lot of other celebrities.\n\nCheck ou which celebrities are born on the same day as you with Celebrities App ! ";
        return shareBody;
    }
}
