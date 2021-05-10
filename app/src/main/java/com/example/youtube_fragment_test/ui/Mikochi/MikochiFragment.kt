package com.example.youtube_fragment_test.ui.Mikochi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.youtube_fragment_test.ChannelInfo
import com.example.youtube_fragment_test.R

class MikochiFragment : Fragment() {

    private lateinit var mikochiViewModel: MikochiViewModel
    var mChannelInfo = ChannelInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mikochiViewModel =
            ViewModelProvider(this).get(MikochiViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mikochi, container, false)
        root.tag = getString(R.string.mikochi)
        //ここにGoogle Cloud Platformで自作したプロジェクトのAPI_keyを入れる
        //「android_keyではなく、ブラウザキーとして作成する」
        mChannelInfo.setAPIKey("")
        //さくらみこのチャンネルID(UC*****→UUに変更で取得可能)
        mChannelInfo.setChannelID("UU-hM6YJuNYVAmUWxeIr9FeA")
        mChannelInfo.setPartPair("snippet")
        mChannelInfo.setMaxResult("50")
        mChannelInfo.getYoutubeParams(this.activity, root)
        return root
    }
}