package com.icoder.twitterproject.ui.Homepage

import android.app.ListActivity
import android.os.Bundle
import com.icoder.twitterproject.R.layout.homepage
import com.icoder.twitterproject.utils.Constants
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter
import com.twitter.sdk.android.tweetui.UserTimeline




class Homepage : ListActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homepage)

        val userId = intent.getLongExtra(Constants.USER_ID_KEY,0)

        val userTimeline = UserTimeline.Builder()
                .userId(userId)
                .build()
        val adapter = TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build()
        listAdapter = adapter

    }


}