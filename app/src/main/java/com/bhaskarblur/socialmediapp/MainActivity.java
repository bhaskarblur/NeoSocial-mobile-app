package com.bhaskarblur.socialmediapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.bhaskarblur.socialmediapp.models.actionRequest;
import com.bhaskarblur.socialmediapp.models.generalRequest;
import com.bhaskarblur.socialmediapp.models.userModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

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
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

//        manageLogic();
//        loadData();

    }

    private void manageLogic() {
        adapter = new postsAdapter(this, postList);
        adapter.setOnClickInterface(new postsAdapter.onClickListener() {
            @Override
            public void onProfileClick(int position_) {
                Bundle bundle = new Bundle();
                bundle.putString("uemail", postList.get(position_).getUserDetails().getPostedBy());
                Intent intent = new Intent(MainActivity.this, userProfile.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }

            @Override
            public void onLikeClick(int position_, int action) {

                likePost(postList.get(position_).getPostid(), String.valueOf(action));
            }

            @Override
            public void onSaveClick(int position_, int action) {
                savePost(postList.get(position_).getPostid(), String.valueOf(action));
            }

            @Override
            public void onCommentClick(int position_) {
                Intent intent = new Intent(MainActivity.this, commentsActivity.class);
                Bundle bundle= new Bundle();
                bundle.putString("postId", postList.get(position_).getPostid());
                intent.putExtra("data", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }

            @Override
            public void onLikesCountClick(int position_) {
                Intent intent = new Intent(MainActivity.this, likesActivity.class);
                Bundle bundle= new Bundle();
                bundle.putString("postId", postList.get(position_).getPostid());
                intent.putExtra("data", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.postList.setLayoutManager(llm);
        binding.postList.setAdapter(adapter);


        binding.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = preferences.getString("userEmail","");
                Bundle bundle = new Bundle();
                bundle.putString("uemail", email);
                Intent intent = new Intent(MainActivity.this, userProfile.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        binding.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, uploadPost.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });


    }

    private void likePost(String postId, String action) {
        String email = preferences.getString("userEmail","");
        String token = preferences.getString("accessToken", "");
        keys _keys = new keys();
        Retrofit client = new Retrofit.Builder().baseUrl(_keys.getApi_baseurl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient api = client.create(apiClient.class);

        actionRequest request = new actionRequest(email,token, action, postId);

        Call<PostListModel> likepostCall = api.likepost(request);

        likepostCall.enqueue(new Callback<PostListModel>() {
            @Override
            public void onResponse(Call<PostListModel> call, Response<PostListModel> response) {

                if(response.code() == 200) {
                    if (response.body() != null) {
                      Log.d("msg", Objects.requireNonNull(response.body().getMessage()));
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
            public void onFailure(Call<PostListModel> call, Throwable t) {

            }
        });
    }

    private void savePost(String postId, String action) {
        String email = preferences.getString("userEmail","");
        String token = preferences.getString("accessToken", "");
        keys _keys = new keys();
        Retrofit client = new Retrofit.Builder().baseUrl(_keys.getApi_baseurl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient api = client.create(apiClient.class);

        actionRequest request = new actionRequest(email,token, action, postId);

        Call<PostListModel> likepostCall = api.savepost(request);

        likepostCall.enqueue(new Callback<PostListModel>() {
            @Override
            public void onResponse(Call<PostListModel> call, Response<PostListModel> response) {

                if(response.code() == 200) {
                    if (response.body() != null) {
                        Log.d("msg", Objects.requireNonNull(response.body().getMessage()));
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
            public void onFailure(Call<PostListModel> call, Throwable t) {

            }
        });
    }
    private void loadData() {
        dialog = ProgressDialog.show(
                this, "",
                "Loading. Please wait...", true
        );
        postList.clear();
        adapter.list.clear();
        String email = preferences.getString("userEmail","");
        String name = preferences.getString("userName","");
        String token = preferences.getString("accessToken", "");
        String pfp = preferences.getString("userPic", "");
        if(!pfp.equals("")) {
            Picasso.get().load(pfp)
                    .resize(120, 120)
                    .transform(new CropCircleTransformation()).into(binding.userIcon);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        manageLogic();
        loadData();
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
