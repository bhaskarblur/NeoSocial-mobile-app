package com.bhaskarblur.socialmediapp.models

import com.google.gson.annotations.SerializedName

class userModel {

    class subUserModel {
        private var username: String = "";
        private var email: String = ""
        @SerializedName("Token")
        private var accessToken: String = "";
        private var profilepic: String = "";
        private var bio: String = "";
        private var status: String = "";

        constructor() {

        }
        constructor(
            username: String,
            email: String,
            accessToken: String,
            profilepic: String,
            bio: String,
            status: String,
            token: String
        ) {
            this.username = username
            this.email = email
            this.accessToken = accessToken
            this.profilepic = profilepic
            this.bio = bio
            this.status = status
        }

        constructor(
            username: String,
            email: String,
            profilepic: String,
            bio: String,
            status: String
        ) {
            this.username = username
            this.email = email
            this.profilepic = profilepic
            this.bio = bio
            this.status = status
        }

        public fun getUsername(): String {
            return username;
        }

        public fun getEmail(): String {
            return email;
        }

        public fun getAccessToken(): String {
            return accessToken;
        }

        public fun getProfilepic(): String {
            return profilepic;
        }

        public fun getBio(): String {
            return bio;
        }

        public fun getToken(): String {
            return accessToken;
        }

        public fun getStatus(): String {
            return status;
        }

    }
    public class responseBody {

        private lateinit var message:String;
        private var userDetails: subUserModel = subUserModel();

        public fun getMessage(): String {
            return message;
        }

        public fun getUserDetails() : subUserModel {
            return userDetails;
        }

        constructor(message: String, userDetails: subUserModel) {
            this.message = message
            this.userDetails = userDetails
        }

        constructor() {

        }

    }

    public class requestBody {

        constructor(email: String, password: String) {
            this.email = email
            this.password = password
        }

        public lateinit var email:String;
        public lateinit var password:String;

        public fun setCreds(email: String, password: String) {
            this.email =email;
            this.password=password;
        }

    }
}