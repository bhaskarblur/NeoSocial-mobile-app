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