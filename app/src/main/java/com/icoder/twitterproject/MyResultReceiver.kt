package com.icoder.twitterproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.twitter.sdk.android.tweetcomposer.TweetUploadService

/**
 * Created by admin on 7/4/18.
 */
class MyResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (TweetUploadService.UPLOAD_SUCCESS == intent.action) {
            // success
            val tweetId = intent.getLongExtra(TweetUploadService.EXTRA_TWEET_ID, 1)
        } else if (TweetUploadService.UPLOAD_FAILURE == intent.action) {
            // failure
            val retryIntent = intent.getParcelableExtra<Parcelable>(TweetUploadService.EXTRA_RETRY_INTENT)
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL == intent.action) {
            // cancel
        }
    }
}