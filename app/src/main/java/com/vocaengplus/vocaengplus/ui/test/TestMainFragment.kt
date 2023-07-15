package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.FragmentTestMainBinding
import com.vocaengplus.vocaengplus.databinding.TesthelpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestMainFragment : Fragment() {

    private var _binding: FragmentTestMainBinding? = null
    private val binding: FragmentTestMainBinding get() = _binding!!
    private val testViewModel: TestViewModel by viewModels()
    private val helpDialog: AlertDialog by lazy {
        val dlgBinding = TesthelpBinding.inflate(layoutInflater)
        AlertDialog.Builder(requireContext())
            .setView(dlgBinding.root)
            .setNeutralButton("확인") { _, _ ->
                {

                }
            }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testWordListSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    testViewModel.selectWordList(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            }

        binding.run {
            vm = testViewModel
            lifecycleOwner = this@TestMainFragment.viewLifecycleOwner
        }

        binding.run {
            helpButton.setOnClickListener {
                helpDialog.show()
            }

            testTypeSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    testViewModel.testTypes
                )

            testStartButton.setOnClickListener {
                findNavController().navigate(R.id.action_testMainFragment_to_testFragment)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.wordLists.collectLatest {
                    binding.testWordListSpinner.adapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item,
                            it.map { it2 -> it2.wordListName }
                        )
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

        testViewModel.getData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}