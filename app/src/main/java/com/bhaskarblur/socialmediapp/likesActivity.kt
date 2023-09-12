package com.bhaskarblur.socialmediapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskarblur.socialmediapp.adapter.commentsAdapter
import com.bhaskarblur.socialmediapp.adapter.likesAdapter
import com.bhaskarblur.socialmediapp.adapter.postsAdapter
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityCommentsBinding
import com.bhaskarblur.socialmediapp.databinding.ActivityLikesBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.CommentsListModel
import com.bhaskarblur.socialmediapp.models.LikesListModel
import com.bhaskarblur.socialmediapp.models.likes;
import com.bhaskarblur.socialmediapp.models.actionRequest
import com.bhaskarblur.socialmediapp.models.comments
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class likesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLikesBinding;
    private var list : ArrayList<likes> = ArrayList();
    private var keys_ = keys();
    private lateinit var adapter : likesAdapter;
    private var preferences: SharedPreferences? = null;
    private var post_id : String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikesBinding.inflate(layoutInflater);
        setContentView(binding.root);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        manageLogic();
        addData();
    }

    private fun addData() {
        var intent : Intent = getIntent();
        var bundle = intent.getBundleExtra("data");
        post_id = bundle?.getString("postId", "").toString();
        val email: String = preferences?.getString("userEmail", "") ?: ""
        val name: String = preferences?.getString("userName", "")?: ""
        val token: String = preferences?.getString("accessToken", "")?: ""
        val client = Retrofit.Builder().baseUrl(keys_.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)
        Log.d("postId", post_id);
        val request = actionRequest(email, token,"0",post_id);

        val commentListModelCall = api.postlikes(request)

        commentListModelCall.enqueue(object : Callback<LikesListModel?> {
            override fun onResponse(
                call: Call<LikesListModel?>,
                response: Response<LikesListModel?>
            ) {
                Log.d("comments", java.lang.String.valueOf(
                    response.code()
                ));
                if (response.code() == 200) {
                    Log.d("Message", response.body()!!.message!!)
                    if (response.body()!!.likes != null) {
                        for (like in response.body()!!.likes) {
                            list.add(like)
                        }

                        adapter.notifyDataSetChanged()
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@likesActivity, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<LikesListModel?>, t: Throwable) {
                Toast.makeText(this@likesActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun manageLogic() {
        val email: String = preferences?.getString("userEmail", "") ?: ""
        val name: String = preferences?.getString("userName", "")?: ""
        val token: String = preferences?.getString("accessToken", "")?: ""
        val pfp: String = preferences?.getString("userPic", "")?: ""
        adapter = likesAdapter(this, list)

        adapter.setOnClickInterface(object : likesAdapter.onClickListener {
            override fun onItemClick(position_: Int) {


            }

        });
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.likesRv.setLayoutManager(llm)
        binding.likesRv.setAdapter(adapter)

        binding.backButton.setOnClickListener {
            finish();
        }
    }

}