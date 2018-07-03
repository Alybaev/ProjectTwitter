package com.icoder.twitterproject.ui.Homepage

import com.twitter.sdk.android.core.models.Tweet
import java.util.ArrayList

// a collection of tweets
class Twitter : ArrayList<Tweet>() {
    companion object {
        private val serialVersionUID = 1L
    }
}