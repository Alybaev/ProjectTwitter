package com.icoder.twitterproject.ui.Homepage

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.icoder.twitterproject.R
import com.icoder.twitterproject.R.layout.homepage
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_ID
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_NAME
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetcomposer.ComposerActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import com.twitter.sdk.android.tweetui.FixedTweetTimeline
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter
import com.twitter.sdk.android.tweetui.UserTimeline
import kotlinx.android.synthetic.main.homepage.*
import kotlinx.android.synthetic.main.toolbar.*


class Homepage : AppCompatActivity() {

    var userId: Long? = null
    var userName: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homepage)
        setSupportActionBar(findViewById(R.id.toolbar))


        init()
        /*    maybe I need this method later */
        //       callTwitterApiClient()


    }

    fun init() {
        initToolbar()
        initReceivedData()
        callTwitterApiClient()
        initProfileImage()
    }

    private fun initProfileImage() {
        val profileImageUrl = "https://twitter.com/${userName}/profile_image?size=bigger"
        Glide.with(this)
                .load(profileImageUrl)
                .into(image_profile)
    }

    fun initReceivedData() {
        userName = intent.getStringExtra(KEY_USER_NAME)
        title = "@" + userName


        userId = intent.getLongExtra(KEY_USER_ID, 0)
    }

    fun callTwitterApiClient() {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val call = statusesService.homeTimeline(null, null, null, null, true, false, true)
        call.enqueue(object : Callback<List<Tweet>>() {
            override fun success(result: Result<List<Tweet>>) {
                initTwitterListAdapter(result.data)
            }

            override fun failure(exception: TwitterException) {
                Log.d("_______EXCEPTION", "Tweets call Exception")
            }
        })
    }

    fun initTwitterListAdapter(data: List<Tweet>) {


        val userTimeline = FixedTweetTimeline.Builder()
                .setTweets(data)
                .build()

        val adapter = TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .setViewStyle(R.style.tw__TweetLightStyle)
                .build()

        list_tweets.adapter = adapter
        swipe_layout_refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            swipe_layout_refresh.isRefreshing = true
            adapter!!.refresh(object : Callback<TimelineResult<Tweet>>() {
                override fun success(result: Result<TimelineResult<Tweet>>) {
                    swipe_layout_refresh.isRefreshing = false
                }

                override fun failure(exception: TwitterException) {
                    "There are no new tweets"
                }
            })
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    fun addTweet(v: View) {
        val builder = TweetComposer.Builder(this)
                .text("What's new?")
        builder.show()
    }

}

