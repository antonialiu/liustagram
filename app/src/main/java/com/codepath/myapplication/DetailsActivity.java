package com.codepath.myapplication;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.myapplication.model.Post;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    Post post;
    public TextView tvUsername;
    public TextView tvTime;
    public TextView tvDescription;
    public ImageView ivPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = (TextView) findViewById(R.id.tvUsername2);
        tvDescription = (TextView) findViewById(R.id.tvDescription2);
        ivPost = (ImageView) findViewById(R.id.ivPost2);
        tvTime = (TextView) findViewById(R.id.tvTime);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        Log.d("MovieDetailsActivity", String.format("Showing details for post'"));

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTime.setText(getRelativeTimeAgo(post));

        Glide.with(getApplicationContext())
                .load(post.getImage().getUrl())
                .into(ivPost);
    }

    public String getRelativeTimeAgo(Post post) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
            long dateMillis = post.getCreatedAt().getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

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

}
