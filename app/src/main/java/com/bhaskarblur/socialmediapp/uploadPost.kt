package com.bhaskarblur.socialmediapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskarblur.socialmediapp.databinding.ActivitySignupBinding
import com.bhaskarblur.socialmediapp.databinding.ActivityUploadPostBinding

class uploadPost : AppCompatActivity() {

    private lateinit var binding : ActivityUploadPostBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPostBinding.inflate(layoutInflater);
        setContentView(binding.root);

        manageUI();
    }

    private fun manageUI() {
    }
}