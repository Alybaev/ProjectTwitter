package com.icoder.twitterproject.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.icoder.twitterproject.R
import com.icoder.twitterproject.model.TweetData
import com.icoder.twitterproject.utils.Constants
import com.twitter.sdk.android.core.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Homepage : Activity() {

    var tweetInfo : ArrayList<TweetData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.homepage)


        val usernId = intent.getLongExtra(Constants.USER_ID_KEY,0)
        getBackData(usernId)

        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService


    }
    fun getBackData(userId : Long){
        NetWork.getW().getTweetsData(userId).enqueue(object : Callback<ArrayList<TweetData>> {
            override fun onResponse(call: Call<ArrayList<TweetData>>?, response: Response<ArrayList<TweetData>>?) {
                if(response!!.isSuccessful) {
                    tweetInfo = response.body()


                } else {
                    Toast.makeText(this@Homepage, "error", Toast.LENGTH_LONG).show()
                }

                Log.d("dd==-", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<TweetData>>?, t: Throwable?) {
                Toast.makeText(this@Homepage,t.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }
}