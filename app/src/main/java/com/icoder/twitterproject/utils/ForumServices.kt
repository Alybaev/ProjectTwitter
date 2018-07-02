package com.icoder.twitterproject.utils

import com.icoder.twitterproject.model.TweetData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForumServices {
    @GET("user_timeline.json?")
    fun getTweetsData( @Query("user_id")userId: Long) : Call<ArrayList<TweetData>>
}
