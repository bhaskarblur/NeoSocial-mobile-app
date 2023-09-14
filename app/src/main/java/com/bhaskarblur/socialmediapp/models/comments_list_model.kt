package com.bhaskarblur.socialmediapp.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CommentsListModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("Comments"   ) @Expose var comments   : ArrayList<comments> = arrayListOf()

    constructor(
        message: String,
        comments: ArrayList<comments>
    ) {

        this.message = message;
        this.comments = comments;
    }

}

class imageListModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("image"   ) @Expose var image   : String? = null

    constructor(
        message: String,
        image: ArrayList<comments>
    ) {

        this.message = message;
        this.image = image.toString();
    }

}

class comments  {

    @SerializedName("email"         ) @Expose var postedBy         : String? = null;
    @SerializedName("username" ) @Expose var postedByUsername : String? = null;
    @SerializedName("profilepic"       ) @Expose var profilepic       : String? = null;
    @SerializedName("comment"       ) @Expose var comment       : String? = null;


    constructor(postedBy: String?, postedByUsername: String?, profilepic: String?, comment : String?) {
        this.postedBy = postedBy
        this.postedByUsername = postedByUsername
        this.profilepic = profilepic
        this.comment = comment;
    }

    constructor() {

    }


}
