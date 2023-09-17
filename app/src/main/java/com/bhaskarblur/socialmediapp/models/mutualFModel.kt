package com.bhaskarblur.socialmediapp.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class MutualFModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("Followers"   ) @Expose var followers  : ArrayList<mutualUser>?;

    constructor(
        message: String,
        followers:  ArrayList<mutualUser>?
    ) {

        this.message = message;
        this.followers = followers;
    }

}


class mutualUser {
    private var username: String = "";
    private var email: String = ""
    private var profilepic: String = "";
    constructor() {

    }
    constructor(
        username: String,
        email: String,
        profilepic: String,

    ) {
        this.username = username
        this.email = email
        this.profilepic = profilepic

    }

    public fun getUsername(): String {
        return username;
    }

    public fun getEmail(): String {
        return email;
    }


    public fun getProfilepic(): String {
        return profilepic;
    }



}
