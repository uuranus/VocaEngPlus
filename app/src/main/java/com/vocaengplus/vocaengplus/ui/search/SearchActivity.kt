package com.vocaengplus.vocaengplus.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val addWordDialog by lazy {
        SearchAddDialog(object : SearchAddDialogListener {
            override fun onWordListClickListener(position: Int) {
                searchViewModel.selectWordList(position)
            }

            override fun onPositiveClickListener() {
                println("positiveClickeD!")
                searchViewModel.saveWord()
            }

            override fun onNegativeClickListener() {

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        binding.run {
            vm = searchViewModel
            lifecycleOwner = this@SearchActivity
        }

        binding.run {

            switchImageButton.setOnClickListener {
                searchViewModel.switch()
            }

            searchButton.setOnClickListener {
                searchViewModel.translate()
            }

            wordAddButton.setOnClickListener {
                addWordDialog.showDialog(supportFragmentManager, searchViewModel.getCurrentWord())
                searchViewModel.getWordLists()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.wordLists.collectLatest {
                    println("ittttt $it")
                    if (it.isNotEmpty()) {
                        addWordDialog.setWordList(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}