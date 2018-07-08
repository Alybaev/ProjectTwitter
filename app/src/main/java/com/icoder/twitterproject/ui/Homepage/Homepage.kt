package com.icoder.twitterproject.ui.Homepage

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.icoder.twitterproject.R
import com.icoder.twitterproject.R.layout.homepage
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_ID
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_NAME
import com.joooonho.SelectableRoundedImageView
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import com.twitter.sdk.android.tweetui.TweetUtils
import com.twitter.sdk.android.tweetui.TweetView
import kotlinx.android.synthetic.main.homepage.*
import kotlinx.android.synthetic.main.toolbar.*


class Homepage : AppCompatActivity() {

    var userId: Long? = null
    var userName: String? = null

    private var twitterApiClient: TwitterApiClient? = null
    private var tweetHomepage: List<Tweet>? = null
    private var statusesService: StatusesService? = null

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
        initTweets()
        initProfileImage()
        swipeLayoutListener()
    }

    private fun initTweets() {
        initTwitterHomelineServices()
        callTwitterApiClient()
        addTweetIntoLayout()
    }


    private fun initProfileImage() {
        var imageProfile = findViewById<SelectableRoundedImageView>(image_profile.id)
        val profileImageUrl = "https://twitter.com/${userName}/profile_image?size=bigger"
        Glide.with(this)
                .load(profileImageUrl)
                .into(imageProfile)
    }

    fun initReceivedData() {
        userName = intent.getStringExtra(KEY_USER_NAME)
        title = "@" + userName

        userId = intent.getLongExtra(KEY_USER_ID, 0)
    }

    fun initTwitterHomelineServices(){
        twitterApiClient = TwitterCore.getInstance().apiClient
        statusesService = twitterApiClient!!.statusesService
    }




    fun callTwitterApiClient() {

        val call = statusesService!!.homeTimeline(null, null, null, true, true, false, true)
        call.enqueue(object : Callback<List<Tweet>>() {
            override fun success(result: Result<List<Tweet>>) {
                for (i in 0 until result.data.size) {
                    tweetHomepage = result.data

                }

            }

            override fun failure(exception: TwitterException) {
                Log.d("_______EXCEPTION", "Tweets call Exception")
            }
        })
    }
    fun addTweetIntoLayout(){
        for(i in 0 until tweetHomepage!!.size) {
            TweetUtils.loadTweet(tweetHomepage!![i].id, object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>) {
                    tweet_layout.addView(TweetView(this@Homepage, result.data))

                }

                override fun failure(exception: TwitterException) {

                }
            })
        }
    }
    fun swipeLayoutListener() {
        swipe_layout_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                swipe_layout_refresh.isRefreshing = true
                refreshTweets()
            }
        })
    }

    private fun refreshTweets() {
        callTwitterApiClient()

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    fun addTweet(v: View) {
        val builder = TweetComposer.Builder(this)
                .text("What's new?")
        builder.show()
    }


}

