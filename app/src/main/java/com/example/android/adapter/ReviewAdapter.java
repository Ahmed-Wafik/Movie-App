package com.example.android.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.movie.R;
import com.example.android.model.Reviews;
import java.util.List;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CardViewHolder> {

    private List<Reviews> reviewsList;
    private Context context;

    public ReviewAdapter(Context context, List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.review_layout, parent, false);

        CardViewHolder cardViewHolder = new CardViewHolder(view);

        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        final Reviews reviews = reviewsList.get(position);
        String author = reviews.getAuthor();
        String content = reviews.getContent();
        holder.author.setText(author);
        holder.content.setText(content);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {


        TextView author, content;
        CardView cardView;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_content);
        }
    }
}
