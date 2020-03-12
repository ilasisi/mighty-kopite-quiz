package com.example.ywna.quiz.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YourScoreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Score Fragment"
    }
    val text: LiveData<String> = _text
}