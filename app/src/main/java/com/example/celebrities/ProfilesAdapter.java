package com.example.celebrities;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfilesAdapter extends ArrayAdapter<Profile> {
    private Context context;
    private List<Profile> list;
    public ProfilesAdapter(Activity context,List<Profile> profiles) {
        super(context,0,profiles);
        this.list = list;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        Profile profile = getItem(position);

        if(profile!=null){
            TextView name = (TextView) rowView.findViewById(R.id.name);
            name.setText(profile.getTitle());

            TextView profession = (TextView) rowView.findViewById(R.id.profession);
            profession.setText(profile.getProfession());

            ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
                    .getImageLoader();

            NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.imageView_image);

            imageLoader.get(profile.getImageURL(), ImageLoader.getImageListener(imageView,android.R.drawable.stat_sys_download ,android.R.drawable.ic_dialog_alert));
            imageView.setImageUrl(profile.getImageURL(), imageLoader);
        }
        return rowView;
    }


}