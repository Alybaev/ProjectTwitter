package com.icoder.twitterproject.ui.Homepage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoder.twitterproject.R
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.tweet_cell.view.*

class HomepageAdapter(var arrayTweets: List<Tweet>) : RecyclerView.Adapter<HomepageAdapter.MViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        return MViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tweet_cell, null, false))
    }

    override fun getItemCount(): Int {
        return arrayTweets.size
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.view_tweet.tweet = arrayTweets[position]




    }

    inner class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view_tweet = view.bike_tweet

    }

    fun setMData(idOfTweet: List<Tweet>) {
        this.arrayTweets = idOfTweet
        notifyDataSetChanged()
    }


}