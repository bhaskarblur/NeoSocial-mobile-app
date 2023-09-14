package com.bhaskarblur.socialmediapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityUploadPicBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.CommentsListModel
import com.bhaskarblur.socialmediapp.models.generalRequest
import com.bhaskarblur.socialmediapp.models.imageListModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPicBinding.inflate(layoutInflater);
        setContentView(binding.root);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        manageUI();
    }

    private fun manageUI() {

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
            if(!selected_uri.equals("")) {
                uploadPic_();
            }
            else {
                Toast.makeText(this@uploadPic, "Please choose a profile pic!", Toast.LENGTH_SHORT).show();
            }
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

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        Log.d("imin", "onClick: in image conversion")
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            Log.d("imin", "onClick: in image conversion try")
            cursor.getString(column_index)
        } finally {
            Log.d("imin", "onClick: in image conversion finally")
            cursor?.close()
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
            "Loading. Please wait...", true
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
                    startActivity(Intent(this@uploadPic, MainActivity::class.java));
                    finish();
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
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101) {
            if(data != null) {
                val extras: Bundle? = data.extras;
                if (extras != null) {
                    //Get image
                    val uri: Uri? = data.data;
                    selected_uri = uri.toString();
                    Picasso.get().load(uri).transform(CropCircleTransformation()).into(binding.pfpimage);

                }
            }
        }

    }
}