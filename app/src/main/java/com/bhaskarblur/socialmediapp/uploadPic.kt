package com.bhaskarblur.socialmediapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityUploadPicBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.PostListModel
import com.bhaskarblur.socialmediapp.models.bioRequest
import com.bhaskarblur.socialmediapp.models.generalRequest
import com.bhaskarblur.socialmediapp.models.imageListModel
import com.bhaskarblur.socialmediapp.models.linkRequest
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.selects.select
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


class uploadPic : AppCompatActivity() {

    private lateinit var binding : ActivityUploadPicBinding;
    private var preferences: SharedPreferences? = null
    private var selected_uri : String = "";
    private var prefs: SharedPreferences? = null
    private var entryType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPicBinding.inflate(layoutInflater);
        setContentView(binding.root);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        manageUI();
        loadData();
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

    private fun loadData() {
        var _intent = intent;
        entryType = _intent.getBundleExtra("data")!!.getString("entry", "");
        val email: String? = preferences!!.getString("userEmail", "")
        val bio: String? = preferences!!.getString("userBio", "")
        val name: String? = preferences!!.getString("userName", "")
        val token: String? = preferences!!.getString("accessToken", "")
        val pfp: String? = preferences!!.getString("userPic", "")
        val link: String? = preferences!!.getString("userLink", "")

        if(bio != null) {
            binding.bioText.setText(bio.toString());
        }
        if(link != null) {
            binding.linkTxt.setText(link.toString());
        }
        if(pfp != null) {
            Picasso.get().load(pfp).transform(CropCircleTransformation()).into(binding.pfpimage);
        }
        if(name != null) {
            binding.usernameTxt.setText(name.toString());
        }
    }

    private fun manageUI() {

        binding.usernameTxt.isEnabled =false
        binding.backButton2.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
        }
        binding.pfpimage.setOnClickListener {

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

        binding.savebtn.setOnClickListener {

            if(binding.bioText.text != null && !binding.bioText.text.isEmpty()) {
                updateBio()
            }
            if(binding.linkTxt.text != null && !binding.linkTxt.text.isEmpty()) {
                updateLink()
            }
            if(!selected_uri.equals("") && selected_uri != null) {
                uploadPic_();
            }

            Handler().postDelayed({
                if(entryType.equals("signup")) {

                }
                else {
                    Toast.makeText(this@uploadPic, "Profile saved", Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        }
    }

    private fun pickImage() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, 101)
    }


    @Throws(Exception::class)
   private fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                (this@uploadPic),
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
            pickImage();
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun uploadPic_() {

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
        var _keys = keys();
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = generalRequest(email!!, token!!);

        var upload = api.uploadpic(email_tosend, token_tosend, file_to_upload);

        val dialog = ProgressDialog.show(
            this@uploadPic, "",
            "Upload. Please wait...", true
        )
        upload.enqueue(object : Callback<imageListModel> {
            override fun onResponse(
                call: Call<imageListModel>,
                response: Response<imageListModel>
            ) {
                dialog.cancel();
                if(response.body()!!.message.toString().contains("Uploaded")) {
                    val editor = prefs!!.edit()
                    editor.putString("userPic", response.body()?.image.toString())
                    editor.apply()
                    editor.commit()
                    if(entryType.equals("signup")) {
                        startActivity(Intent(this@uploadPic, MainActivity::class.java));
                        finish();
                    }
                }
                else {
                    Toast.makeText(this@uploadPic, "There was an error uploading image!"
                    , Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<imageListModel>, t: Throwable) {
                Toast.makeText(this@uploadPic, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun updateBio() {

        val email: String? = preferences!!.getString("userEmail", "")
        val name: String? = preferences!!.getString("userName", "")
        val token: String? = preferences!!.getString("accessToken", "")
        val pfp: String? = preferences!!.getString("userPic", "")

        var _keys = keys();
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = bioRequest(email!!, token!!, binding.bioText.text.toString());

        var upload = api.setbio(request);


        upload.enqueue(object : Callback<PostListModel> {
            override fun onResponse(
                call: Call<PostListModel>,
                response: Response<PostListModel>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message.toString().contains("updated")) {
                        val editor = prefs!!.edit()
                        editor.putString("userBio", binding.bioText.text.toString())
                        editor.apply()
                        editor.commit()
                    } else {
                        Toast.makeText(
                            this@uploadPic,
                            "There was an error uploading image!",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
            override fun onFailure(call: Call<PostListModel>, t: Throwable) {
                Toast.makeText(this@uploadPic, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun updateLink() {

        val email: String? = preferences!!.getString("userEmail", "")
        val name: String? = preferences!!.getString("userName", "")
        val token: String? = preferences!!.getString("accessToken", "")
        val pfp: String? = preferences!!.getString("userPic", "")

        var _keys = keys();
        val client = Retrofit.Builder().baseUrl(_keys.api_baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = client.create(apiClient::class.java)

        val request = linkRequest(email!!, token!!, binding.linkTxt.text.toString());

        var upload = api.setlink(request);


        upload.enqueue(object : Callback<PostListModel> {
            override fun onResponse(
                call: Call<PostListModel>,
                response: Response<PostListModel>
            ) {
                if (response.body() != null) {
                if(response.body()!!.message.toString().contains("updated")) {
                    val editor = prefs!!.edit()
                    editor.putString("userLink", binding.linkTxt.text.toString())
                    editor.apply()
                    editor.commit()
                }
                else {
                    Toast.makeText(this@uploadPic, "There was an error uploading image!"
                        , Toast.LENGTH_SHORT).show();
                }
            }
                }
            override fun onFailure(call: Call<PostListModel>, t: Throwable) {
                Toast.makeText(this@uploadPic, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101) {
            if(data != null) {
                val extras: Bundle? = data.extras;
                if (extras != null || data.data != null) {
                    //Get image
                    val uri: Uri? = data.data;
                    selected_uri = uri.toString();
                    Picasso.get().load(uri).transform(CropCircleTransformation()).into(binding.pfpimage);

                }
            }
        }

    }
}