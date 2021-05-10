package com.example.youtube_fragment_test.ui.Pekora

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PekoraViewModel : ViewModel() {
    //以下ボトムナビゲーションでのプロジェクト作成のデフォルト
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}