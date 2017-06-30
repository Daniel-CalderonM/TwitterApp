package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    ImageButton button;
    private SwipeRefreshLayout swipeContainer;


    //Request COde
    private final int REQUEST_CODE = 20;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_tweet, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tweet);
        //define button for reply
        button = (ImageButton) findViewById(R.id.btnReply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimelineActivity.this,TwitterReply.class);
                startActivity(i);
            }
        });
        setContentView(R.layout.activity_timeline);
        client =TwitterApp.getRestClient();
        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct the adapater from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("TwitterClient",response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.d("TwitterClient",response.toString());
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
            @Override
            public void onFailure(int statusCode,Header[] headers, String responseString,Throwable throwable){
                Log.d("TwitterClient",responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                Intent i = new Intent(this,ComposeActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getParcelableExtra("TWEET");
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);


        }
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        tweetAdapter.clear();
        populateTimeline();
        swipeContainer.setRefreshing(false);

    }


}
