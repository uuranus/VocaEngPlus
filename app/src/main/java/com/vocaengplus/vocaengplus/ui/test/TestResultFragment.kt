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
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.adapter.TestResultListAdapter
import com.vocaengplus.vocaengplus.databinding.FragmentTestResultBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TestResultFragment : Fragment() {
    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!
    private val testViewModel: TestViewModel by activityViewModels()
    private val testResultAdapter: TestResultListAdapter = TestResultListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            vm = testViewModel
            lifecycleOwner = this@TestResultFragment.viewLifecycleOwner
            resultRecyclerView.adapter = testResultAdapter
        }

        binding.run {
            endTestButton.setOnClickListener {
                testViewModel.saveTestResult()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.isSavedSuccess.collectLatest {
                    if (it) {
                        requireActivity().finish()
//                        findNavController().navigate(R.id.action_testResultFragment_to_testMainFragment)
                    }
                }
            }
        }

    }

}