package com.vocaengplus.vocaengplus.ui.review

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vocaengplus.vocaengplus.adapter.ReviewListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityReviewBinding
import com.vocaengplus.vocaengplus.databinding.DialogWordListInfoBinding
import com.vocaengplus.vocaengplus.databinding.ReviewhelpBinding
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.util.toDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewListAdapter().apply {
        itemClickListener = object : ReviewListAdapter.OnItemClickListener {
            override fun onItemClick(
                position: Int,
            ) {
                reviewViewModel.setBlackBox(position)
                reviewViewModel.resetBlackBox()
            }

            override fun onArrowClick(position: Int) {
                reviewViewModel.getWordListInfo(position)
            }
        }
    }

    private val helpDialog: AlertDialog by lazy {
        val dlgBinding = ReviewhelpBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setNeutralButton("확인", null)
            .create()
    }

    private val wordListDialogView: DialogWordListInfoBinding by lazy {
        DialogWordListInfoBinding.inflate(layoutInflater)
    }
    private val wordListInfoDialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setView(wordListDialogView.root)
            .setNeutralButton("확인", null)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        reviewViewModel.getReviews()
    }

    private fun init() {
        binding.run {
            vm = reviewViewModel
            lifecycleOwner = this@ReviewActivity
            reviewRecyclerView.adapter = reviewAdapter
        }

        binding.helpbutton.setOnClickListener {
            helpDialog.show()
        }

        binding.blackBox.setOnClickListener {
            reviewViewModel.showBlackBox()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reviewViewModel.currentWordListInfo.collectLatest {
                    if (it.wordListUid.isNotEmpty()) {
                        setWordListInfo(it)
                        wordListInfoDialog.show()
                    }
                }
            }
        }
    }

    private fun setWordListInfo(wordList: WordList) {
        wordListDialogView.run {
            titleTextView.text = "제목: ${wordList.wordListName}"
            downloadDateTextView.text = "다운로드 날짜 : ${wordList.downLoadDate.toDateString()}"
            descriptionTextView.text = "내용 : ${wordList.description}"
        }
    }

}