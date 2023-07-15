package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vocaengplus.vocaengplus.databinding.FragmentTestBinding
import com.vocaengplus.vocaengplus.model.data.Voca
import java.util.Random

class TestFragment(val category: String, val data: ArrayList<Voca>, val type: String) : Fragment() {
    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding get() = _binding!!

    private val testViewModel: TestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            vm = testViewModel
            lifecycleOwner = this@TestFragment.viewLifecycleOwner
        }

        binding.run {

        }

        testViewModel.getWords()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}