package com.bhaskarblur.socialmediapp.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class LikesListModel {

    @SerializedName("message" ) @Expose var message : String?          = null;
    @SerializedName("Likes"   ) @Expose var likes   : ArrayList<likes> = arrayListOf()

    constructor(
        message: String,
        likes: ArrayList<likes>
    ) {

        this.message = message;
        this.likes = likes;
    }

}


class likes  {

    @SerializedName("email"         ) @Expose var postedBy         : String? = null;
    @SerializedName("username" ) @Expose var postedByUsername : String? = null;
    @SerializedName("profilepic"       ) @Expose var profilepic       : String? = null;


    constructor(postedBy: String?, postedByUsername: String?, profilepic: String?) {
        this.postedBy = postedBy
        this.postedByUsername = postedByUsername
        this.profilepic = profilepic
    }

    constructor() {

    }


}
