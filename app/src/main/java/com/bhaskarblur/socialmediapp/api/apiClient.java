package com.bhaskarblur.socialmediapp.api;

import com.bhaskarblur.socialmediapp.models.PostListModel;
import com.bhaskarblur.socialmediapp.models.generalRequest;
import com.bhaskarblur.socialmediapp.models.userModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface apiClient {

    @POST("users/login")
    Call<userModel.responseBody> login(@Body userModel.requestBody user);

    @POST("posts/post-feed")
    Call<PostListModel> homefeed(@Body generalRequest request);
}
