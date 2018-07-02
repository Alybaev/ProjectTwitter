package com.icoder.twitterproject.utils

import com.icoder.twitterproject.model.TweetData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForumServices {
    @GET("forecast?")
    fun getTweetsData( @Query("q")name: String,@Query("mode") mode: String) : Call<TweetData>
}
