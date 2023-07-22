package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

}