package com.icoder.twitterproject.ui.Homepage

import android.os.Bundle
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
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import kotlinx.android.synthetic.main.homepage.*
import kotlinx.android.synthetic.main.toolbar.*
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import com.twitter.sdk.android.tweetui.*
import com.miguelcatalan.materialsearchview.MaterialSearchView


class Homepage : AppCompatActivity() {


    var userId: Long? = null
    var userName: String? = null
    var listTweets: List<Tweet>? = null
    private var UserTimelineAdapter: TweetTimelineListAdapter? =  null
    private var userTimeline: SearchTimeline? = null
    private var searchTimelineAdapter: TweetTimelineListAdapter? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homepage)
        setSupportActionBar(findViewById(R.id.toolbar))


        init()

    }

    fun init() {
        initToolbar()
        initReceivedData()
        initProfileImage()
        callTwitterApiClient()


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

    fun initTwitterUserListAdapter() {
        getUserTweets()
    }

    fun initTwitterUserListAdapter(v: View) {
        list_tweets.adapter = UserTimelineAdapter
    }



    fun getUserTweets() {
        val userTimeline = FixedTweetTimeline.Builder()
                .setTweets(listTweets)
                .build()

        UserTimelineAdapter = TweetTimelineListAdapter.Builder(this@Homepage)
                .setTimeline(userTimeline)
                .setViewStyle(R.style.tw__TweetLightStyle)
                .build()

        list_tweets.adapter = UserTimelineAdapter

        swipe_layout_refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            swipe_layout_refresh.isRefreshing = true
            UserTimelineAdapter!!.refresh(object : Callback<TimelineResult<Tweet>>() {
                override fun success(result: Result<TimelineResult<Tweet>>) {
                    swipe_layout_refresh.isRefreshing = false
                }

                override fun failure(exception: TwitterException) {
                    "There are no new tweets"
                }
            })
        })
    }

    fun callTwitterApiClient() {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val call = statusesService.homeTimeline(null, null, null, true, true, true, true)
        call.enqueue(object : Callback<List<Tweet>>() {
            override fun success(result: Result<List<Tweet>>) {
                listTweets = result.data
                initTwitterUserListAdapter()
            }

            override fun failure(exception: TwitterException) {
                Log.d("_______EXCEPTION", "Tweets call Exception")
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setSearchListener()
    }



    private fun setSearchListener() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                userTimeline = SearchTimeline.Builder()
                        .query(query)
                        .build()


                searchTimelineAdapter = TweetTimelineListAdapter.Builder(this@Homepage)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightStyle)
                        .build()

                list_tweets.adapter = searchTimelineAdapter
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //Do some magic
                return false
            }
        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                //Do some magic
            }

            override fun onSearchViewClosed() {
                //Do some magic
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val item = menu.findItem(R.id.action_search)
        search_view.setMenuItem(item)

        return true
    }


}

