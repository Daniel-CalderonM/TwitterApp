package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.TweetListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;


public class TimelineActivity extends AppCompatActivity implements TweetListFragment.TweetSelectedListener {

    ViewPager vpPager;
    TabLayout tabLayout;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        //set the adpater for the view pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),this));
        //setup the tablout to use the view pager
        tabLayout=(TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

    }

    public void onProfileView(MenuItem item) {
        //launch the profile view
        Intent i  = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }
    public void onCompose(MenuItem item){
        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i,20);
        finish();
    }
    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tweet.getUser().getScreenName());
        startActivity(i);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 20:
                Tweet tweet = (Tweet) data.getParcelableExtra("TWEET");
                TweetListFragment tweetListFragment = (TweetListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":0");
                Log.i("Info",tweet.getUser().getScreenName()+" ");
                tweetListFragment.updateTimeline(tweet);
                finish();
                break;
        }
    }
}
