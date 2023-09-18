package com.bhaskarblur.socialmediapp.models

import com.google.gson.annotations.SerializedName

class generalRequest {


        constructor(email: String, token: String) {
            this.email = email
            this.token = token
        }

    @SerializedName("email")
        public lateinit var email:String;
    @SerializedName("token")
        public lateinit var token:String;

}

class actionRequest {


    constructor(email: String, token: String
    , action:String,  postId:String) {
        this.email = email
        this.token = token
        this.action = action;
        this. postId = postId
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("action")
    public lateinit var action:String;
    @SerializedName("postid")
    public lateinit var postId:String;

}

class followRequest {


    constructor(email: String, token: String
                , action:String,  fuseremail:String) {
        this.email = email
        this.token = token
        this.action = action;
        this. fuseremail = fuseremail
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("action")
    public lateinit var action:String;
    @SerializedName("fuserid")
    public lateinit var fuseremail:String;

}
class commentRequest {


    constructor(email: String, token: String
                , comment:String,  postId:String) {
        this.email = email
        this.token = token
        this.comment = comment;
        this. postId = postId
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("comment")
    public lateinit var comment:String;
    @SerializedName("postid")
    public lateinit var postId:String;

}


class deletePostRequest {


    constructor(email: String, token: String,postId:String) {
        this.email = email
        this.token = token
        this. postId = postId
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("postid")
    public lateinit var postId:String;

}
class profileRequest {


    constructor(email: String, token: String, uemail:String) {
        this.email = email
        this.token = token
        this.uemail = uemail
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;
    @SerializedName("uemail")
    public lateinit var uemail:String;

}

class mutualFollowersRequest {


    constructor(email: String, token: String, uemail:String) {
        this.email = email
        this.token = token
        this.uemail = uemail
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;
    @SerializedName("fuserid")
    public lateinit var uemail:String;

}

class linkRequest {


    constructor(email: String, token: String, link:String) {
        this.email = email
        this.token = token
        this. link = link
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("link")
    public lateinit var link:String;

}


class bioRequest {


    constructor(email: String, token: String, bio:String) {
        this.email = email
        this.token = token
        this. bio = bio
    }

    @SerializedName("email")
    public lateinit var email:String;
    @SerializedName("token")
    public lateinit var token:String;

    @SerializedName("bio")
    public lateinit var bio:String;

}