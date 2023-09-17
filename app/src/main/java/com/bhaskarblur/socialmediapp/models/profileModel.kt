package com.bhaskarblur.socialmediapp.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ProfileModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("userDetails"   ) @Expose var userDetails_  : _userDetails_?;

    constructor(
        message: String,
        userDetails_: _userDetails_?
    ) {

        this.message = message;
        this.userDetails_ = userDetails_;
    }

}


class Posts_ {

    @SerializedName("posttext"      ) @Expose var posttext      : String?      = null;
    @SerializedName("image"         ) @Expose var image         : String?      = null;
    @SerializedName("location"      ) @Expose var location      : String?      = null;
    @SerializedName("postid"        ) @Expose var postid        : String?      = null;
    @SerializedName("isLiked"       ) @Expose var isLiked       : Int?         = null;
    @SerializedName("isSaved"       ) @Expose var isSaved       : Int?         = null;
    @SerializedName("commentsCount" ) @Expose var commentsCount : Int?         = null;
    @SerializedName("likesCount"    ) @Expose var likesCount    : Int?         = null;


    constructor(
        posttext: String?,
        image: String?,
        location: String?,
        postid: String?,
        isLiked: Int?,
        isSaved: Int?,
        commentsCount: Int?,
        likesCount: Int?
    ) {
        this.posttext = posttext
        this.image = image
        this.location = location
        this.postid = postid
        this.isLiked = isLiked
        this.isSaved = isSaved
        this.commentsCount = commentsCount
        this.likesCount = likesCount
    }

    constructor() {

    }
}

class _userDetails_ {
    private var username: String = "";
    private var email: String = ""
    @SerializedName("Token")
    private var accessToken: String = "";
    private var profilepic: String = "";
    private var bio: String = "";
    private var status: String = "";
    @SerializedName("link")
    private var link: String = "";

    private  var followersCount: Int = 0;
    private  var isFollowed: Int = 0;
    private  var followingCount: Int = 0;
    @SerializedName("posts")
    private var posts: ArrayList<Posts> = arrayListOf()
    constructor() {

    }
    constructor(
        username: String,
        email: String,
        accessToken: String,
        profilepic: String,
        bio: String,
        status: String,
        token: String,
        link: String,
        posts: ArrayList<Posts>,
        followersCount : Int,
        followingCount: Int,
        isFollowed: Int
    ) {
        this.username = username
        this.email = email
        this.accessToken = accessToken
        this.profilepic = profilepic
        this.bio = bio
        this.status = status
        this.link = link
        this.posts = posts
        this.followingCount = followingCount
        this.followersCount = followersCount
        this.isFollowed = isFollowed
    }

    constructor(
        username: String,
        email: String,
        profilepic: String,
        bio: String,
        status: String,
        link: String,
    ) {
        this.username = username
        this.email = email
        this.profilepic = profilepic
        this.bio = bio
        this.status = status
        this.link = link
    }

    public fun getUsername(): String {
        return username;
    }

    public fun getFollowersCount(): Int {
        return followersCount;
    }

    public fun getFollowingCount(): Int {
        return followingCount;
    }
    public fun getIsFollowed(): Int {
        return isFollowed;
    }
    public fun getLink(): String {
        return link;
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

    public fun getPosts() : ArrayList<Posts> {
        return posts;
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
