package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.TwitterReply;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by calderond on 7/3/17.
 */
public class TweetListFragment extends Fragment implements TweetAdapter.TweetAdapterListener{
    TwitterClient client;

    public interface  TweetSelectedListener{
        //handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    ImageButton button;
    FloatingActionButton fab;
    //Request COde
    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;
    //Inflation happens inside onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        client = TwitterApp.getRestClient();

        View v2 = inflater.inflate(R.layout.item_tweet,container,false);

        button = (ImageButton) v2.findViewById(R.id.btnReply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),TwitterReply.class);
                startActivity(i);
            }
        });
        //INflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list,container, false);
        //define button for reply
        //find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct the adapater from this datasource
        tweetAdapter = new TweetAdapter(tweets,this);
        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                tweetAdapter.clear();

                Log.i("info","Passed");
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimelineActivity)getActivity()).onCompose();
            }
        });

        return v;
    }
    public void addItems(JSONArray response){
        //iterate through the JSON array
        //for each entery, desirialize the JSON object
        for(int i = 0 ;  i < response.length();i++){
            //convert each object ot a Tweet model
            //add that Tweet model to our data source
            //notify the adapter that we have added an item
            try{
                Tweet tweet=  Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size()-1);

            }catch( JSONException e){
                e.printStackTrace();
            }
        }
    }
    public void updateTimeline(Tweet tweet){
        tweets.add(0,tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }
    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
    }
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                List<Tweet> list= new ArrayList<>();
                JSONArray array = (JSONArray) response;
                if (array != null) {
                    for (int i=0;i<array.length();i++){
                        try {
                            list.add(Tweet.fromJSON(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                tweetAdapter.addAll(list);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());

            }
        });
    }


}
