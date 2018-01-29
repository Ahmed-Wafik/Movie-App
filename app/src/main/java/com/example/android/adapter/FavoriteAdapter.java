package com.example.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.Utilities.NetworkUtils;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Ahmed Wafik Mohamed on 1/23/2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private Cursor cursor;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, int position) {

        int poster_path_index = cursor.getColumnIndex("poster_path");
        int title_index = cursor.getColumnIndex("title");


        cursor.moveToPosition(position);

        String title = cursor.getString(title_index);
        String poster_path = cursor.getString(poster_path_index);

        holder.title.setText(title);
        String imagePath = NetworkUtils.buildURL_Image(poster_path);

        if (!imagePath.isEmpty()) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    holder.poster_image.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch swatch = checkVibrantSwatch(palette);
                            if (swatch != null) {
                                holder.title.setBackgroundColor(swatch.getRgb());
                                holder.title.setTextColor(swatch.getBodyTextColor());
                            }
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context)
                    .load(imagePath)
                    .error(R.mipmap.ic_launcher)
                    .into(target);
        }

    }


    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster_image;
        private TextView title;

        public FavoriteViewHolder(View itemView) {
            super(itemView);

            poster_image = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.movie_title_TV);
        }
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = cursor;
        this.cursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    private Palette.Swatch checkVibrantSwatch(Palette p) {
        Palette.Swatch swatch = null;

        if ((p.getVibrantSwatch()) != null) {
            swatch = p.getVibrantSwatch();

        } else if ((p.getMutedSwatch()) != null) {
            swatch = p.getMutedSwatch();

        }
        return swatch;
    }
}
