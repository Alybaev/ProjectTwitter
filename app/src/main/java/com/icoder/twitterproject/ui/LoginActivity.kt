package com.icoder.twitterproject.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.icoder.twitterproject.R
import com.icoder.twitterproject.ui.Homepage.Homepage
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_NAME
import com.icoder.twitterproject.utils.Constants.Companion.KEY_USER_ID
import com.twitter.sdk.android.core.*
import kotlinx.android.synthetic.main.activity_login.*
import com.twitter.sdk.android.core.Twitter
import kotlinx.android.synthetic.main.toolbar.*


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Twitter.initialize(this)
        setContentView(R.layout.activity_login)
        title = "Twitter"
        setSupportActionBar(findViewById(R.id.toolbar))

        /*
          Adding a callback to loginButton
          These statements will execute when loginButton is clicked
         */
        login_button.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                /*
                  This provides TwitterSession as a result
                  This will execute when the authentication is successful
                 */
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken
                val token = authToken.token
                val secret = authToken.secret

                //Calling login method and passing twitter session
                login(session)
            }

            override fun failure(exception: TwitterException) {
                //Displaying Toast message
                Log.e("________", exception.toString())
                Toast.makeText(this@LoginActivity, "Authentication failed!", Toast.LENGTH_LONG).show()
            }
        }

    }


    fun login(session: TwitterSession) {

        val intent = Intent(this@LoginActivity, Homepage::class.java)
        intent.putExtra(KEY_USER_ID, session.userId)
        intent.putExtra(KEY_USER_NAME, session.userName)

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int , data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the login button.
        login_button.onActivityResult(requestCode, resultCode, data)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

}
