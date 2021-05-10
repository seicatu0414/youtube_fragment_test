package com.example.youtube_fragment_test.ui.Mikochi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MikochiViewModel : ViewModel() {
    //以下ボトムナビゲーションでのプロジェクト作成のデフォルト
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}