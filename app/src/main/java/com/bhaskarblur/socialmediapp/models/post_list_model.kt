package com.bhaskarblur.socialmediapp.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PostListModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("posts"   ) @Expose var posts   : ArrayList<Posts> = arrayListOf()

    constructor(
        message: String,
        posts: ArrayList<Posts>
    ) {

        this.message = message;
        this.posts = posts;
    }

}


class Posts {

    @SerializedName("posttext"      ) @Expose var posttext      : String?      = null;
    @SerializedName("image"         ) @Expose var image         : String?      = null;
    @SerializedName("location"      ) @Expose var location      : String?      = null;
    @SerializedName("postid"        ) @Expose var postid        : String?      = null;
    @SerializedName("userDetails"   ) @Expose var userDetails   : UserDetails? = UserDetails();
    @SerializedName("isLiked"       ) @Expose var isLiked       : Int?         = null;
    @SerializedName("isSaved"       ) @Expose var isSaved       : Int?         = null;
    @SerializedName("commentsCount" ) @Expose var commentsCount : Int?         = null;
    @SerializedName("likesCount"    ) @Expose var likesCount    : Int?         = null;


    constructor(
        posttext: String?,
        image: String?,
        location: String?,
        postid: String?,
        userDetails: UserDetails?,
        isLiked: Int?,
        isSaved: Int?,
        commentsCount: Int?,
        likesCount: Int?
    ) {
        this.posttext = posttext
        this.image = image
        this.location = location
        this.postid = postid
        this.userDetails = userDetails
        this.isLiked = isLiked
        this.isSaved = isSaved
        this.commentsCount = commentsCount
        this.likesCount = likesCount
    }

    constructor() {

    }
}

class UserDetails  {

    @SerializedName("postedBy"         ) @Expose var postedBy         : String? = null;
    @SerializedName("postedByUsername" ) @Expose var postedByUsername : String? = null;
    @SerializedName("profilepic"       ) @Expose var profilepic       : String? = null;


    constructor(postedBy: String?, postedByUsername: String?, profilepic: String?) {
        this.postedBy = postedBy
        this.postedByUsername = postedByUsername
        this.profilepic = profilepic
    }

    constructor() {

    }


}
