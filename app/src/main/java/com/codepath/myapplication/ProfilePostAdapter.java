package com.codepath.myapplication;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.myapplication.model.Post;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {


    private static List<Post> posts;
    static Context context;

    public ProfilePostAdapter(List<Post> postList) {
        posts = postList;
    }

    @NonNull
    public ProfilePostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_profile_posts, viewGroup, false);
        ProfilePostAdapter.ViewHolder viewHolder = new ProfilePostAdapter.ViewHolder(postView);
        return viewHolder;
    }

    public void onBindViewHolder(ProfilePostAdapter.ViewHolder viewHolder, int i) {
        Post post = posts.get(i);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPost);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (relativeDate.contains("minute")) {
            relativeDate = relativeDate.split("i")[0];
        }

        if (relativeDate.contains("hour")) {
            relativeDate = relativeDate.split("o")[0];
        }

        if (relativeDate.contains("second")) {
            relativeDate = relativeDate.split("e")[0];
        }

        return relativeDate;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivPost;
        public TextView tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPost = (ImageView) itemView.findViewById(R.id.ivGrid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
