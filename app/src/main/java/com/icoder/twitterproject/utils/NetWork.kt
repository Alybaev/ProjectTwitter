package com.icoder.twitterproject.utils

import android.app.Application
import com.icoder.twitterproject.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetWork : Application() {
    companion object {
        var forum: ForumServices? = null
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        fun getW() : ForumServices {
            forum = retrofit.create(ForumServices::class.java)
            return forum!!
        }

    }
}