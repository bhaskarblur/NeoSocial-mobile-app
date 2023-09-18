package com.bhaskarblur.socialmediapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bhaskarblur.socialmediapp.adapter.postsAdapter
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityUserProfileBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.MutualFModel
import com.bhaskarblur.socialmediapp.models.PostListModel
import com.bhaskarblur.socialmediapp.models.Posts
import com.bhaskarblur.socialmediapp.models.ProfileModel
import com.bhaskarblur.socialmediapp.models.actionRequest
import com.bhaskarblur.socialmediapp.models.deletePostRequest
import com.bhaskarblur.socialmediapp.models.followRequest
import com.bhaskarblur.socialmediapp.models.mutualFollowersRequest
import com.bhaskarblur.socialmediapp.models.profileRequest
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects


class userProfile : AppCompatActivity() {

    private val postList = ArrayList<Posts>()
    private val mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var binding: ActivityUserProfileBinding
    private var adapter: postsAdapter? = null
    var dialog: ProgressDialog? = null
    private var preferences: SharedPreferences? = null
    private var uemail : String =""
    private var prefs: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater);
        setContentView(binding.root);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var _intent = intent;
        var bundle_ = _intent.getBundleExtra("data");
        uemail = bundle_!!.getString("uemail", "");
        manageLogic();

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

    private fun manageLogic() {
        val email = preferences!!.getString("userEmail", "")
        binding.editIcon.setOnClickListener(View.OnClickListener {
            if(uemail == email) {
                val intent = Intent(this@userProfile, uploadPic::class.java)
                val bundle = Bundle()
                bundle.putString("entry", "profile");
                intent.putExtra("data", bundle)
                startActivity(intent)
            }
        })
        binding.userLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.userLink.text.toString()))
            startActivity(browserIntent)
        }
        binding.backButton4.setOnClickListener {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

        binding.followBtn.setOnClickListener {
            if(binding.followBtn.text.equals("Following")) {
                followUser(1.toString());
                binding.followersCount.setText((binding.followersCount.text.toString().toIntOrNull()!! - 1).toString());
            }
            else {
                followUser(0.toString());
                binding.followersCount.setText((binding.followersCount.text.toString().toIntOrNull()!! + 1).toString());
            }
        }


        adapter = postsAdapter(this, postList, email!!)
        adapter!!.setOnClickInterface(object : postsAdapter.onClickListener {
            override fun onDeleteClick(position_: Int) {
                val alertDialogBuilder = AlertDialog.Builder(this@userProfile)
                alertDialogBuilder.setMessage("Are you sure you want to delete this post")
                alertDialogBuilder.setPositiveButton(
                    "Yes"
                ) { dialogInterface, i ->
                    deletePost(postList[position_].postid!!)
                    postList.removeAt(position_)
                    adapter!!.notifyDataSetChanged()

                }.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> }
                alertDialogBuilder.show()
            }

            override fun onLikeClick(position_: Int, action: Int) {
                likePost(postList[position_].postid!!, action.toString())
            }

            override fun onProfileClick(position_: Int) {
            }

            override fun onSaveClick(position_: Int, action: Int) {
                savePost(postList[position_].postid!!, action.toString())
            }

            override fun onCommentClick(position_: Int) {
                val intent = Intent(this@userProfile, commentsActivity::class.java)
                val bundle = Bundle()
                bundle.putString("postId", postList[position_].postid)
                intent.putExtra("data", bundle)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            }

            override fun onLikesCountClick(position_: Int) {
                val intent = Intent(this@userProfile, likesActivity::class.java)
                val bundle = Bundle()
                bundle.putString("postId", postList[position_].postid)
                intent.putExtra("data", bundle)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            }
        })
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.postList.layoutManager = llm
        binding.postList.adapter = adapter

    }


    private fun likePost(postId: String, action: String) {
        val email = preferences!!.getString("userEmail", "")
        val token = preferences!!.getString("accessToken", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = actionRequest(email!!, token!!, action, postId)
        val likepostCall = api.likepost(request)
        likepostCall.enqueue(object : Callback<PostListModel?> {
            override fun onResponse(
                call: Call<PostListModel?>,
                response: Response<PostListModel?>
            ) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        Objects.requireNonNull(
                            response.body()!!.message
                        )?.let {
                            Log.d(
                                "msg", it
                            )
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@userProfile, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostListModel?>, t: Throwable) {}
        })
    }

    private fun deletePost(postId: String) {
        val email = preferences!!.getString("userEmail", "")
        val token = preferences!!.getString("accessToken", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = deletePostRequest(email!!, token!!, postId);
        val likepostCall = api.deletepost(request)
        likepostCall.enqueue(object : Callback<PostListModel?> {
            override fun onResponse(
                call: Call<PostListModel?>,
                response: Response<PostListModel?>
            ) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        Objects.requireNonNull(
                            response.body()!!.message
                        )?.let {
                            Log.d(
                                "msg", it
                            )
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@userProfile, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostListModel?>, t: Throwable) {}
        })
    }
    private fun mutualFollowers() {
        val email = preferences!!.getString("userEmail", "")
        val token = preferences!!.getString("accessToken", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = mutualFollowersRequest(email!!, token!!,uemail)
        val likepostCall = api.mutualfollowers(request)
        likepostCall.enqueue(object : Callback<MutualFModel?> {
            override fun onResponse(
                call: Call<MutualFModel?>,
                response: Response<MutualFModel?>
            ) {
                Log.d("code_", response.code().toString());
                if (response.code() == 200) {

                    if (response.body() != null) {

                        if(response.body()!!.followers!!.size>0) {
                            if(response.body()!!.followers!!.size>1) {
                                binding.followedbyTxt.setText("Followed by "+
                                        response.body()!!.followers!!.get(0).getUsername() + ", "+
                                        response.body()!!.followers!!.get(1).getUsername() +" ...")
                            }
                            else   {
                                binding.followedbyTxt.setText("Followed by "+
                                response.body()!!.followers!!.get(0).getUsername())
                            }
                        }
                        else {
                            binding.followedbyLayout.visibility = View.GONE;
                        }
                        Objects.requireNonNull(
                            response.body()!!.message
                        )?.let {
                            Log.d(
                                "msg", it
                            )
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@userProfile, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<MutualFModel?>, t: Throwable) {}
        })
    }

    private fun savePost(postId: String, action: String) {
        val email = preferences!!.getString("userEmail", "")
        val token = preferences!!.getString("accessToken", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = actionRequest(email!!, token!!, action, postId)
        val likepostCall = api.savepost(request)
        likepostCall.enqueue(object : Callback<PostListModel?> {
            override fun onResponse(
                call: Call<PostListModel?>,
                response: Response<PostListModel?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Objects.requireNonNull(
                            response.body()!!.message
                        )?.let {
                            Log.d(
                                "msg", it
                            )
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@userProfile, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostListModel?>, t: Throwable) {}
        })
    }
    private fun loadData() {
        dialog = ProgressDialog.show(
            this, "",
            "Loading. Please wait...", true
        )
        val email = preferences!!.getString("userEmail", "")
        val name = preferences!!.getString("userName", "")
        val bio = preferences!!.getString("userBio", "")
        val link = preferences!!.getString("userLink", "")
        val token = preferences!!.getString("accessToken", "")
        val pfp = preferences!!.getString("userPic", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = profileRequest(email!!, token!!, uemail)
        val profile = api.userprofile(request)

        profile.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                if(response.code() == 200) {
                    dialog!!.cancel()
                    var body = response.body();

                    for (post_ in response.body()!!.userDetails_!!.getPosts()) {
                        postList.add(post_)
                    }
                    adapter!!.notifyDataSetChanged()
                    mutualFollowers();
                    var details = response.body()!!.userDetails_;
                    binding.userName.setText(details!!.getUsername().toString())
                    binding.userBio.setText(details!!.getBio())
                    binding.userLink.setText(details!!.getLink())
                    binding.postcount.setText(postList.size.toString());
                    binding.followersCount.setText(details!!.getFollowersCount().toString());
                    binding.followingCount.setText(details!!.getFollowingCount().toString());

                    var editor : SharedPreferences.Editor = prefs!!.edit();
                    editor.putString("userBio", details!!.getBio());
                    editor.putString("userLink",details!!.getLink());
                    editor.putString("userPic", details!!.getProfilepic());
                    editor.apply()
                    editor.commit()
                    if(!details!!.getProfilepic().isEmpty() &&
                        details!!.getProfilepic() !=null) {
                        Picasso.get().load( details!!.getProfilepic())
                            .resize(500, 500)
                            .transform(CropCircleTransformation()).into(binding.pfpimage2)

                    }
                    if(uemail.toString().equals(email)) {
                        binding.followBtn.setVisibility(View.GONE);
                        binding.messageBtn.setVisibility(View.GONE);
                        binding.editIcon.visibility = View.VISIBLE;

                    }
                    else {
                        binding.editIcon.visibility = View.GONE;
                    }
                    if(details!!.getIsFollowed() == 1) {
                        binding.followBtn.setText("Following");
                        binding.followBtn.setBackgroundColor(Color.BLACK);

                    }
                    else {
                    }

                }
            }

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                dialog!!.cancel()
                Toast.makeText(this@userProfile, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }


        })

    }

    private fun followUser(i: String) {
        val email = preferences!!.getString("userEmail", "")
        val token = preferences!!.getString("accessToken", "")
        val _keys = keys()
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = client.create(apiClient::class.java)
        val request = followRequest(email!!, token!!, i, uemail)
        val likepostCall = api.follow(request)
        likepostCall.enqueue(object : Callback<PostListModel?> {
            override fun onResponse(
                call: Call<PostListModel?>,
                response: Response<PostListModel?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {

                        if(i.toString().equals("1")) {
                            binding.followBtn.setText("Follow");
                            binding.followBtn.setBackgroundColor(resources.getColor(R.color.primary)
                            )
                        }
                        else {
                            binding.followBtn.setText("Following");
                            binding.followBtn.setBackgroundColor(Color.BLACK);
                        }
                        Objects.requireNonNull(
                            response.body()!!.message
                        )?.let {
                            Log.d(
                                "msg", it
                            )
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@userProfile, response
                                .body()!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostListModel?>, t: Throwable) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadData();
    }
}