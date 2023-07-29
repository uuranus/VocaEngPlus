package com.vocaengplus.vocaengplus.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.vocaengplus.vocaengplus.databinding.ActivityProfileBinding
import com.vocaengplus.vocaengplus.ui.register.RegisterActivity
import com.vocaengplus.vocaengplus.util.Validation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var imagePickActivityLauncher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        binding.run {
            vm = profileViewModel
            lifecycleOwner = this@ProfileActivity
        }

        binding.apply {

            profileProfileUpload.setOnClickListener {
                Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_PICK
                }.run {
                    imagePickActivityLauncher?.launch(this)
                }
            }

            savebtn.setOnClickListener {
                profileViewModel.saveNewProfile()
            }
        }

        imagePickActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val videoContentUri = result.data?.data ?: return@registerForActivityResult
                    profileViewModel.setNewProfileImage(videoContentUri)
                }
            }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.isGuest.collectLatest {
                    if (it) {
                        val intent = Intent(this@ProfileActivity, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.isSaveSuccess.collectLatest {
                    if (it) {
                        finish()
                    }
                }
            }
        }

        profileViewModel.getCurrentProfile()
    }
}
