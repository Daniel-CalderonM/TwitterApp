package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    public TextView tvTweet;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client =TwitterApp.getRestClient();
        tvTweet = (TextView) findViewById(R.id.tvTweet);
        ButterKnife.bind(this);
        button = (Button) findViewById(R.id.btnTweet);
        getSupportActionBar().hide();

    }
    @OnClick(R.id.btnTweet)
    public void onClick() {
        client.sendTweet(tvTweet.getText().toString(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet newTweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    i.putExtra("TWEET",newTweet);
                    setResult(20,i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

