package com.codepath.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.codepath.myapplication.ProfilePostAdapter;
import com.codepath.myapplication.R;
import com.codepath.myapplication.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    MenuItem bLogout;

    protected ProfilePostAdapter postAdapter;
    protected ArrayList<Post> posts;
    RecyclerView rvPosts2;
    protected SwipeRefreshLayout swipeContainer;
    TextView username;
    ImageView ivPropic2;

    // private EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.tvUsernameP);
        ivPropic2 = view.findViewById(R.id.ivProPic2);
        rvPosts2 = (RecyclerView) view.findViewById(R.id.rvPosts2);
        bLogout = (MenuItem) view.findViewById(R.id.bLogout);
        posts = new ArrayList<>();
        postAdapter = new ProfilePostAdapter(posts);
        rvPosts2.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvPosts2.setAdapter(postAdapter);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer2);

        populateTimeline();

        /*bLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onLogoutClick(menuItem);
                return true;
            }
        });*/

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                fetchTimelineAsync(0);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected void populateTimeline() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postsQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("TimelineActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                        posts.add(objects.get(i));
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });

        ParseFile photofile = (ParseFile) ParseUser.getCurrentUser().get("propic");

        username.setText(ParseUser.getCurrentUser().getUsername());

        if (photofile != null) {
            Glide.with(getActivity())
                    .load(photofile.getUrl())
                    .placeholder(R.drawable.empty_profile)
                    .into(ivPropic2);
        }
        else {
            Glide.with(getActivity())
                    .load(R.drawable.empty_profile)
                    .into(ivPropic2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.logout, menu);
        // getMenuInflater().inflate(R.menu.compose, menu);
    }

    /*public void onCompose(MenuItem mi) {
        compose(mi.getActionView());
    }

    public void compose(View view) {
        final Intent intent = new Intent(TimelineActivity.this, PostActivity.class);
        startActivity(intent);
    }*/


    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        postAdapter.clear();
        // ...the data has come back, add new items to your adapter...
        populateTimeline();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

}
