package com.bhaskarblur.socialmediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bhaskarblur.socialmediapp.adapter.postsAdapter;
import com.bhaskarblur.socialmediapp.api.apiClient;
import com.bhaskarblur.socialmediapp.databinding.ActivityLoginscreenBinding;
import com.bhaskarblur.socialmediapp.databinding.ActivityMainBinding;
import com.bhaskarblur.socialmediapp.env.keys;
import com.bhaskarblur.socialmediapp.models.PostListModel;
import com.bhaskarblur.socialmediapp.models.Posts;
import com.bhaskarblur.socialmediapp.models.generalRequest;
import com.bhaskarblur.socialmediapp.models.userModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Posts> postList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActivityMainBinding binding;
    private postsAdapter adapter;
    ProgressDialog dialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        manageLogic();
        loadData();
        dialog = ProgressDialog.show(
                this, "",
                "Loading. Please wait...", true
        );
    }

    private void manageLogic() {
        adapter = new postsAdapter(this, postList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.postList.setLayoutManager(llm);
        binding.postList.setAdapter(adapter);
    }

    private void loadData() {

        String email = preferences.getString("userEmail","");
        String name = preferences.getString("userName","");
        String token = preferences.getString("accessToken", "");
        binding.userName.setText("Hello "+name +"!");
        keys _keys = new keys();
       Retrofit client = new Retrofit.Builder().baseUrl(_keys.getApi_baseurl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient api = client.create(apiClient.class);

        generalRequest request = new generalRequest(email,token);

        Call<PostListModel> postListModelCall = api.homefeed(request);

        postListModelCall.enqueue(new Callback<PostListModel>() {
            @Override
            public void onResponse(Call<PostListModel> call, Response<PostListModel> response) {
                dialog.cancel();
                Log.d("email", String.valueOf(request.getToken()));
                Log.d("code", String.valueOf(response.body()));
                if(response.code() == 200) {
                    Log.d("Message", response.body().getMessage());
                    if(response.body().getPosts() != null) {
                        for(Posts post_: response.body().getPosts() ) {
                            postList.add(post_);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, response
                                .body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostListModel> call, Throwable t) {
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onRefresh() {
        postList.clear();
        loadData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
