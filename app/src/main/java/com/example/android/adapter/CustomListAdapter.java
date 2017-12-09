package com.example.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movie.R;
import com.example.android.Utilities.NetworkUtils;
import com.example.android.model.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed Wafik Mohamed on 12/8/2017.
 */

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private List<Videos> videosList;
    private LayoutInflater layoutInflater;
    private ImageView imageView;

    public CustomListAdapter(Context context, List<Videos> videosList) {
        this.context = context;
        this.videosList = videosList;
    }

    @Override
    public int getCount() {
        return videosList.size();
    }

    @Override
    public Videos getItem(int i) {
        return videosList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.videos_layout, viewGroup, false);
        }
        if (imageView == null) {
            imageView = view.findViewById(R.id.img_video);
        }

        String urlYoutube = NetworkUtils.buildURL_Youtube(videosList.get(i).getKey());
        String thumbnail = "https://img.youtube.com/vi/" + videosList.get(i).getKey() + "/2.jpg";

        Picasso.with(context).load(thumbnail).into(imageView);

        return view;
    }
}