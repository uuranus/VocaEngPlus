package com.vocaengplus.vocaengplus.ui.search

import androidx.lifecycle.ViewModel
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WordRepository,
) : ViewModel() {


    fun translate() {

    }
}