package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TwitterReply extends AppCompatActivity {
    TextView tvUser;
    Tweet tweet;
    TextView tvTweet;
    private TwitterClient client;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_reply);
        tvUser = (TextView) findViewById(R.id.tvUser);
        client = TwitterApp.getRestClient();
        button = (Button) findViewById(R.id.btnReply);
        tvTweet = (TextView) findViewById(R.id.tvReply);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("TWEET"));
        tvUser.setText("Replying to @"+tweet.getUser().getScreenName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.replyTweet(tweet.getUid(),"@"+tweet.getUser().getScreenName()+" "+tvTweet.getText().toString(),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        finish();
                    }
                });
            }
        });
    }

}
