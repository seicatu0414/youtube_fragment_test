package com.example.youtube_fragment_test.ui.Pekora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.youtube_fragment_test.ChannelInfo
import com.example.youtube_fragment_test.R


class PekoraFragment : Fragment() {

    private lateinit var pekoraViewModel: PekoraViewModel
    var mChannelInfo = ChannelInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pekoraViewModel =
            ViewModelProvider(this).get(PekoraViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_pekora, container, false)
        //どのフラグメントの確認様タグ
        root.tag = getString(R.string.pekora)
        //ここにGoogle Cloud Platformで自作したプロジェクトのAPI_keyを入れる
        //「android_keyではなく、ブラウザキーとして作成する」
        mChannelInfo.setAPIKey("")
        //兎田ぺこらのチャンネルID(UC*****→UUに変更で取得可能)
        mChannelInfo.setChannelID("UU1DCedRgGHBdm81E1llLhOQ")
        mChannelInfo.setPartPair("snippet")
        mChannelInfo.setMaxResult("50")
        mChannelInfo.getYoutubeParams(this.activity, root)
        return root
    }
}