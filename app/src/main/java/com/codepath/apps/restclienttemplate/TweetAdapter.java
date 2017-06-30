package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by calderond on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    private List<Tweet> mTweets;
    Context context;
    public static ImageButton repButton;
    TwitterClient client;
    //pass in Tweets array in the constructor
    public TweetAdapter (List<Tweet> tweets){
        mTweets = tweets;
    }
    //for each row, inflate the layout and cahce refrences into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        client =TwitterApp.getRestClient();
        return viewHolder;
    }

    //bind the values based on the postion of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        final Tweet tweet = mTweets.get(position);

        //populate the view according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvHandlename.setText(" @ " + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(tweet.timeStamp);
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        holder.button.setOnClickListener(onRelpy(tweet));


    }
    private View.OnClickListener onRelpy(final Tweet tweet){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TwitterReply.class);
                i.putExtra("TWEET", Parcels.wrap(tweet));
                ((AppCompatActivity) context).startActivity(i);
            }
        };
    }
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvHandlename;
        public TextView tvTimestamp;
        public ImageButton button;
        public ViewHolder(View itemView){
            super(itemView);

            //perform findViewById lookups
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvHandlename = (TextView) itemView.findViewById(R.id.tvHandelname);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            button = (ImageButton) itemView.findViewById(R.id.btnReply);
        }

    }
    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }


    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
