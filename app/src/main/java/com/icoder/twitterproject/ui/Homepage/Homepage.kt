package com.icoder.twitterproject.ui.Homepage

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.gson.Gson
import java.io.*;
import java.net.URLEncoder;

import com.icoder.twitterproject.utils.Constants.Companion.CONSUMER_KEY
import com.icoder.twitterproject.utils.Constants.Companion.CONSUMER_SECRET
import com.twitter.sdk.android.core.Twitter
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.client.ClientProtocolException
import cz.msebera.android.httpclient.params.BasicHttpParams
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient
import cz.msebera.android.httpclient.client.methods.HttpRequestBase
import com.twitter.sdk.android.core.models.Tweet
import cz.msebera.android.httpclient.client.methods.HttpGet


class Homepage : ListActivity() {
    private var activity: ListActivity? = null
    val ScreenName = "therockncoder"
    val LOG_TAG = "rnc"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this

        downloadTweets()
    }

    // download twitter timeline after first checking to see if there is a network connection
    fun downloadTweets() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            DownloadTwitterTask().execute(ScreenName)
        } else {
            Log.v(LOG_TAG, "No network connection available.")
        }
    }

    // Uses an AsyncTask to download a Twitter user's timeline
    private inner class DownloadTwitterTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg screenNames: String): String? {
            var result: String? = null

            if (screenNames.size > 0) {
                result = getTwitterStream(screenNames[0])
            }
            return result
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        override fun onPostExecute(result: String) {
            var twits = jsonToTwitter(result)

            // send the tweets to the adapter for rendering
            val adapter = ArrayAdapter<Tweet>(twits, activity, android.R.layout.simple_list_item_1,twits)
            listAdapter = adapter
        }

        // converts a string of JSON data into a Twitter object
        private fun jsonToTwitter(result: String?): Twitter? {
            var twits: Twitter? = null
            if (result != null && result.length > 0) {
                try {
                    val gson = Gson()
                    twits = gson.fromJson<Twitter>(result, Twitter::class.java!!)
                } catch (ex: IllegalStateException) {
                    // just eat the exception
                }

            }
            return twits
        }

        // convert a JSON authentication object into an Authenticated object
        private fun jsonToAuthenticated(rawAuthorization: String?): Authenticated? {
            var auth: Authenticated? = null
            if (rawAuthorization != null && rawAuthorization.length > 0) {
                try {
                    val gson = Gson()
                    auth = gson.fromJson<Authenticated>(rawAuthorization, Authenticated::class.java!!)
                } catch (ex: IllegalStateException) {
                    // just eat the exception
                }

            }
            return auth
        }

        private fun getResponseBody(request: HttpRequestBase): String {
            val sb = StringBuilder()
            try {

                val httpClient = DefaultHttpClient(BasicHttpParams())
                val response = httpClient.execute(request)
                val statusCode = response.statusLine.statusCode
                val reason = response.statusLine.reasonPhrase

                if (statusCode == 200) {

                    val entity = response.entity
                    val inputStream = entity.content

                    val bReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"), 8)
                    var line: String? = null
                    while ((line == bReader.readLine()) != null) {
                        sb.append(line)
                    }
                } else {
                    sb.append(reason)
                }
            } catch (ex: UnsupportedEncodingException) {
            } catch (ex1: ClientProtocolException) {
            } catch (ex2: IOException) {
            }

            return sb.toString()
        }

        private fun getTwitterStream(screenName: String): String? {
            var results: String? = null

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                val urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8")
                val urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8")

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                val combined = urlApiKey + ":" + urlApiSecret

                // Base64 encode the string
                val base64Encoded = Base64.encodeToString(combined.toByteArray(), Base64.NO_WRAP)

                // Step 2: Obtain a bearer token
                val httpPost = HttpPost(TwitterTokenURL)
                httpPost.setHeader("Authorization", "Basic " + base64Encoded)
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                httpPost.setEntity(StringEntity("grant_type=client_credentials"))
                val rawAuthorization = getResponseBody(httpPost)
                val auth = jsonToAuthenticated(rawAuthorization)

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth!!.token_type.equals("bearer")) {

                    // Step 3: Authenticate API requests with bearer token
                    val httpGet = HttpGet(TwitterStreamURL + screenName)

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth!!.access_token)
                    httpGet.setHeader("Content-Type", "application/json")
                    // update the results with the body of the response
                    results = getResponseBody(httpGet)
                }
            } catch (ex: UnsupportedEncodingException) {
            } catch (ex1: IllegalStateException) {
            }

            return results
        }


        internal val TwitterTokenURL = "https://api.twitter.com/oauth2/token"
        internal val TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="

    }


}