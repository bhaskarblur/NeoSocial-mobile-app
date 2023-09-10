package com.bhaskarblur.socialmediapp

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bhaskarblur.socialmediapp.api.apiClient
import com.bhaskarblur.socialmediapp.databinding.ActivityLoginscreenBinding
import com.bhaskarblur.socialmediapp.env.keys
import com.bhaskarblur.socialmediapp.models.userModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class loginscreen : AppCompatActivity() {

    private lateinit var binding : ActivityLoginscreenBinding;
    private lateinit var client: Retrofit;
    private var prefs: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = ActivityLoginscreenBinding.inflate(layoutInflater);
        manageLogic();
        //      getActionBar().hide();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(binding.root);

    }

    private fun manageLogic() {

        var keys = keys();
        binding.loginBtn.setOnClickListener {

            if(binding.emailTxt.text.trim().toString().isEmpty()) {
               binding.emailTxt.setError("Enter email");
            }
            else if(binding.passTxt.toString().isEmpty()) {
                binding.passTxt.setError("Enter password");
            }
            else {
                val dialog = ProgressDialog.show(
                    this@loginscreen, "",
                    "Loading. Please wait...", true
                )

                client = Retrofit.Builder().baseUrl(keys.getApi_baseurl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                var api = client.create(apiClient::class.java)

                var user = userModel.requestBody(binding.emailTxt.text.toString()
                , binding.passTxt.text.toString());

                Log.d("user", user.email);
                Log.d("user", user.password);

                var login = api.login(user);

                login.enqueue(object : Callback<userModel.responseBody> {
                    override fun onResponse(
                        call: Call<userModel.responseBody>,
                        response: Response<userModel.responseBody>
                    ) {

                      var respbody = response.body();
                        dialog.cancel();
                        Log.d("body", response.body()?.getMessage().toString());
                        if(response.body()!=null) {

                            if(response.body()?.getMessage().toString().contains("Login successful!")) {
                                Toast.makeText(this@loginscreen, respbody?.getMessage().toString(), Toast.LENGTH_SHORT).show()
                                Log.d("userDetails", response.body()?.getUserDetails()?.getEmail().toString());

                                val editor = prefs!!.edit()
                                editor.putBoolean("loggedStatus", true);
                                editor.putString("accessToken", response.body()?.getUserDetails()?.getAccessToken().toString());
                                editor.putString("userEmail", response.body()?.getUserDetails()?.getEmail().toString())
                                editor.putString("userName", response.body()?.getUserDetails()?.getUsername().toString())
                                editor.putString("userPic", response.body()?.getUserDetails()?.getProfilepic().toString())
                                editor.apply()
                                editor.commit()
                                startActivity(Intent(this@loginscreen, MainActivity::class.java));
                                finish();

                            }


                            else {
                                Toast.makeText(this@loginscreen,
                                    response.body()?.getMessage().toString(), Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                    override fun onFailure(call: Call<userModel.responseBody>, t: Throwable) {
                        dialog.cancel();
                        Log.d("err", t.toString());
                        Toast.makeText(this@loginscreen, t.message.toString(), Toast.LENGTH_SHORT).show();
                    }

                })

            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}