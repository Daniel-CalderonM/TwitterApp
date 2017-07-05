package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by calderond on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    //return the totla number of fragments there are
    private String tabTitles []= new String[] {"Home","Mentions"};
    private Context context;
    @Override
    public int getCount() {
        return 2;
    }
    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context  = context;
    }
    //return the fragment to use depending on position

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new HomeTimelineFragment();
        }else if(position == 1)
            return  new MentionsTimelineFragment();
        else
            return null;
    }

    //return the fragment titlet thats going to use


    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }
}
