package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.FragmentTestBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TestFragment : Fragment() {
    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding get() = _binding!!

    private val testViewModel: TestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            vm = testViewModel
            lifecycleOwner = this@TestFragment.viewLifecycleOwner
        }

        binding.run {
            answerRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                val pos = when (checkedId) {
                    R.id.radioButton -> 0
                    R.id.radioButton2 -> 1
                    R.id.radioButton3 -> 2
                    R.id.radioButton4 -> 3
                    else -> -1
                }
                if (pos == -1) return@setOnCheckedChangeListener

                testViewModel.checkMyAnswer(pos)
                group.clearCheck()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.moveToResult.collectLatest {
                    if (it) findNavController().navigate(R.id.action_testFragment_to_testResultFragment)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        testViewModel.getTest()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}