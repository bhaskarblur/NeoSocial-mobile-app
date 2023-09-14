package com.bhaskarblur.socialmediapp.api;

import com.bhaskarblur.socialmediapp.models.CommentsListModel;
import com.bhaskarblur.socialmediapp.models.LikesListModel;
import com.bhaskarblur.socialmediapp.models.PostListModel;
import com.bhaskarblur.socialmediapp.models.actionRequest;
import com.bhaskarblur.socialmediapp.models.commentRequest;
import com.bhaskarblur.socialmediapp.models.generalRequest;
import com.bhaskarblur.socialmediapp.models.imageListModel;
import com.bhaskarblur.socialmediapp.models.userModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface apiClient {

    @POST("users/login")
    Call<userModel.responseBody> login(@Body userModel.requestBody user);

    @POST("users/signup")
    Call<userModel.responseBody> signup(@Body userModel.signuprequestBody user);


    @POST("posts/post-feed")
    Call<PostListModel> homefeed(@Body generalRequest request);

    @POST("posts/post-comments")
    Call<CommentsListModel> postcomments(@Body actionRequest request);

    @POST("posts/post-likes")
    Call<LikesListModel> postlikes(@Body actionRequest request);
    @POST("posts/like-post")
    Call<PostListModel> likepost(@Body actionRequest request);

    @POST("posts/save-post")
    Call<PostListModel> savepost(@Body actionRequest request);

    @POST("posts/comment-on-post")
    Call<CommentsListModel> commentpost(@Body commentRequest request);

    @POST("posts/delete-comment")
    Call<CommentsListModel> deletecomment(@Body commentRequest request);

    @Multipart
    @POST("users/upload-profile-pic")
    Call<imageListModel> uploadpic(@Part MultipartBody.Part email, @Part MultipartBody.Part token, @Part MultipartBody.Part file);
}
