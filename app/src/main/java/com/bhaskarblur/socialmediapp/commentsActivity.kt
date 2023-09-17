package com.bhaskarblur.socialmediapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskarblur.socialmediapp.adapter.commentsAdapter
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityCommentsBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.CommentsListModel
import com.bhaskarblur.socialmediapp.models.actionRequest
import com.bhaskarblur.socialmediapp.models.commentRequest
import com.bhaskarblur.socialmediapp.models.comments
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class commentsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCommentsBinding;
    private var list : ArrayList<comments> = ArrayList();
    private var keys_ = keys();
    private lateinit var adapter : commentsAdapter;
    private var preferences: SharedPreferences? = null;
    private var post_id : String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater);
        setContentView(binding.root);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        manageLogic();
        addData();
        //      getActionBar().hide();
        val window = window

// clear FLAG_TRANSLUCENT_STATUS flag:

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color

// finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)


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

        val commentListModelCall = api.postcomments(request)

        commentListModelCall.enqueue(object : Callback<CommentsListModel?> {
            override fun onResponse(
                call: Call<CommentsListModel?>,
                response: Response<CommentsListModel?>
            ) {
                Log.d("comments", java.lang.String.valueOf(
                    response.code()
                ));
                if (response.code() == 200) {
                    Log.d("Message", response.body()!!.message!!)
                    if (response.body()!!.comments != null) {
                        for (comment in response.body()!!.comments) {
                            list.add(comment)
                        }

                        adapter.notifyDataSetChanged()
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@commentsActivity, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<CommentsListModel?>, t: Throwable) {
                Toast.makeText(this@commentsActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun manageLogic() {
        val email: String = preferences?.getString("userEmail", "") ?: ""
        val name: String = preferences?.getString("userName", "")?: ""
        val token: String = preferences?.getString("accessToken", "")?: ""
        val pfp: String = preferences?.getString("userPic", "")?: ""
        adapter = commentsAdapter(this, list)

        adapter.setOnClickInterface(object : commentsAdapter.onClickListener {
            override fun onDeleteClick(position_: Int) {

                var dialog = AlertDialog.Builder(this@commentsActivity)
                    .setMessage("Are you sure you want to delete the comment?")
                    .setPositiveButton("Yes",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                deleteComment(list.get(position_).comment, post_id)
                                list.removeAt(position_);
                                Toast.makeText(this@commentsActivity, "Comment deleted", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        })
                    .setNegativeButton(
                        "No",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {

                            }
                        });

                dialog.show();

            }

        });
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.commentsRv.setLayoutManager(llm)
        binding.commentsRv.setAdapter(adapter)

        binding.backButton.setOnClickListener {
            finish();
        }

        binding.send.setOnClickListener {
            addComment(binding.commentBox.text.toString());
            list.add(comments(email,name, pfp, binding.commentBox.text.toString() ));
            Toast.makeText(this, "Comment posted", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            binding.commentBox.setText("");
        }
    }

    private fun addComment(text: String) {
        var intent : Intent = getIntent();
        var bundle = intent.getBundleExtra("data");
        post_id = bundle?.getString("postId", "").toString();
        val email: String = preferences?.getString("userEmail", "") ?: ""
        val name: String = preferences?.getString("userName", "")?: ""
        val token: String = preferences?.getString("accessToken", "")?: ""

        val client: Retrofit = Retrofit.Builder().baseUrl(keys_.getApi_baseurl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = commentRequest(email, token, text, post_id)

        val likepostCall = api.commentpost(request)

        likepostCall.enqueue(object : Callback<CommentsListModel?> {
            override fun onResponse(
                call: Call<CommentsListModel?>,
                response: Response<CommentsListModel?>
            ) {
                Log.d("added!", response.code().toString());
                if (response.code() == 200) {
                    if (response.body() != null) {
//                        Log.d("msg", Objects.requireNonNull(response.body()!!.message))
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@commentsActivity, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<CommentsListModel?>, t: Throwable) {}
        })
    }

    private fun deleteComment(comment: String?, postid: String) {
        var intent : Intent = getIntent();
        var bundle = intent.getBundleExtra("data");
        post_id = bundle?.getString("postId", "").toString();
        val email: String = preferences?.getString("userEmail", "") ?: ""
        val name: String = preferences?.getString("userName", "")?: ""
        val token: String = preferences?.getString("accessToken", "")?: ""

        val client: Retrofit = Retrofit.Builder().baseUrl(keys_.getApi_baseurl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = comment?.let { commentRequest(email, token, it, post_id) }

        val deleteCall = api.deletecomment(request)

        deleteCall.enqueue(object : Callback<CommentsListModel?> {
            override fun onResponse(
                call: Call<CommentsListModel?>,
                response: Response<CommentsListModel?>
            ) {
                Log.d("deleted!", response.code().toString());
                if (response.code() == 200) {
                    if (response.body() != null) {
//                        Log.d("msg", Objects.requireNonNull(response.body()!!.message))
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@commentsActivity, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<CommentsListModel?>, t: Throwable) {}
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}