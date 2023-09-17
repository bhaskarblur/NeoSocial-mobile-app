package com.bhaskarblur.socialmediapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityUploadPostBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.generalRequest
import com.bhaskarblur.socialmediapp.models.imageListModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream


class uploadPost : AppCompatActivity() {

    private lateinit var binding : ActivityUploadPostBinding;
    private var preferences: SharedPreferences? = null
    private var selected_uri : String = "";
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPostBinding.inflate(layoutInflater);
        setContentView(binding.root);
        setContentView(binding.root);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        manageUI();
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

    private fun manageUI() {

        binding.backButton3.setOnClickListener {
            finish()
        }

        binding.pickImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val result: Int =
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

                if (result == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                }
                else {
                    requestPermissionForReadExtertalStorage();
                }
            }
        }

        binding.postImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val result: Int =
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

                if (result == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                }
                else {
                    requestPermissionForReadExtertalStorage();
                }
            }
        }

        binding.postBtn.setOnClickListener {
            if(binding.captionText.text.toString().isEmpty()) {
                binding.captionText.setError("Please write caption");
            }
            else if (binding.locationText.text.toString().isEmpty()) {
                binding.locationText.setError("Please write location");
            }
            else {
                uploadPost_();
            }
        }
    }

    private fun pickImage() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, 101)
    }

    private fun uploadPost_() {

        val fileDir = applicationContext.filesDir
        var file = File(filesDir, "image.png")
        val input_stream = contentResolver.openInputStream(Uri.parse(selected_uri));
        val outputStream = FileOutputStream(file);
        input_stream!!.copyTo(outputStream)

        var requestBody = file.asRequestBody("image/*".toMediaType());
        val file_to_upload = MultipartBody.Part.createFormData("image", file.name, requestBody);

        val email: String? = preferences!!.getString("userEmail", "")
        val name: String? = preferences!!.getString("userName", "")
        val token: String? = preferences!!.getString("accessToken", "")
        val pfp: String? = preferences!!.getString("userPic", "")

        val email_tosend = MultipartBody.Part.createFormData("email", email!!);
        val token_tosend = MultipartBody.Part.createFormData("token", token!!);
        val text_tosend = MultipartBody.Part.createFormData("posttext",
            binding.captionText.text.toString());
        val location_tosend = MultipartBody.Part.createFormData("location",
            binding.locationText.text.toString());

        var _keys = keys();
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = generalRequest(email!!, token!!);

        var upload = api.uploadpost(email_tosend, token_tosend, text_tosend, location_tosend, file_to_upload);

        val dialog = ProgressDialog.show(
            this@uploadPost, "",
            "Uploading. Please wait...", true
        )
        upload.enqueue(object : Callback<imageListModel> {
            override fun onResponse(
                call: Call<imageListModel>,
                response: Response<imageListModel>
            ) {
                dialog.cancel();
                if(response.body()!!.message.toString().contains("Post") ||
                    response.code() == 200) {
                    Toast.makeText(this@uploadPost, "Post uploaded successfully!"
                        , Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(this@uploadPost, "There was an error uploading image!"
                        , Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<imageListModel>, t: Throwable) {
                Toast.makeText(this@uploadPost, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    @Throws(Exception::class)
    private fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                (this@uploadPost),
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
            pickImage();
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right,
            R.anim.slide_out_right);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101) {
            if(data != null) {
                Log.d("code", data.data.toString());
                val extras: Bundle? = data.extras;
                if (extras != null) {
                    Log.d("code", "not empty");
                    //Get image
                    val uri: Uri? = data.data;
                    selected_uri = uri.toString();
                    binding.postImage.setVisibility(View.VISIBLE);
                    binding.pickImage.setVisibility(View.GONE);
                    Picasso.get().load(uri).into(binding.postImage);

                }
                else if(data.data != null) {
                    val uri: Uri? = data.data;
                    selected_uri = uri.toString();
                    binding.postImage.setVisibility(View.VISIBLE);
                    binding.pickImage.setVisibility(View.GONE);
                    Picasso.get().load(uri).into(binding.postImage);

                }
            }
        }

    }
}