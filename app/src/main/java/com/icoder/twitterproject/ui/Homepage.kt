package com.icoder.twitterproject.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.icoder.twitterproject.R
import com.twitter.sdk.android.core.*
import kotlinx.android.synthetic.main.homepage.*
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService


class Homepage : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Twitter.initialize(this)
        setContentView(R.layout.homepage)

        /* fetching the username from LoginActivity */
        val username = intent.getLongExtra("username",0)
       // TV_username.text = username

        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val call = statusesService.show(524971209851543553L, true, true, true)
        call.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {


                Log.e("____succ",   result.data.text)
            }

            override fun failure(exception: TwitterException) {
                //Do something on failure
                Log.e("____", exception.toString())
            }
        })
    }
}