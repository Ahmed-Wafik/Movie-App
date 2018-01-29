package com.example.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.Utilities.NetworkUtils;
import com.example.android.model.Video;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed Wafik Mohamed on 12/8/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosVH> {

    private Context context;
    private List<Video> videosResponseList;

    public VideosAdapter(Context context, List<Video> videosResponseList) {
        this.context = context;
        this.videosResponseList = videosResponseList;
    }

    @Override
    public VideosVH onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.videos_layout, parent, false);

        return new VideosVH(view);
    }

    @Override
    public void onBindViewHolder(VideosVH holder, int position) {
        String key = videosResponseList.get(position).getKey();
        final String thumbnail = NetworkUtils.buildURL_thumbnail(key);
        final String uriYOUTUBE = NetworkUtils.buildURL_Youtube(key);
        Picasso.with(context).load(thumbnail).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriYOUTUBE));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return videosResponseList.size();
    }

    public class VideosVH extends RecyclerView.ViewHolder {

        ImageView imageView;

        public VideosVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_video);
        }
    }
}
