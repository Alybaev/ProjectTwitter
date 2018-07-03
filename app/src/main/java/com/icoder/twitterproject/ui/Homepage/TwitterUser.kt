package com.icoder.twitterproject.ui.Homepage

/**
 * Created by admin on 7/3/18.
 */
import com.google.gson.annotations.SerializedName


class TwitterUser {

    @SerializedName("screen_name")
    var screenName: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("profile_image_url")
    var profileImageUrl: String? = null
}