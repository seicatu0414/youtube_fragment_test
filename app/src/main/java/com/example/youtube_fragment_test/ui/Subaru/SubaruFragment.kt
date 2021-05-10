package com.example.youtube_fragment_test.ui.Subaru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.youtube_fragment_test.ChannelInfo
import com.example.youtube_fragment_test.R

class SubaruFragment : Fragment() {

    private lateinit var subaruViewModel: SubaruViewModel
    var mChannelInfo = ChannelInfo()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        subaruViewModel =
                ViewModelProvider(this).get(SubaruViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_subaru, container, false)
        root.tag = getString(R.string.subaru)
        //ここにGoogle Cloud Platformで自作したプロジェクトのAPI_keyを入れる
        //「android_keyではなく、ブラウザキーとして作成する」
        mChannelInfo.setAPIKey("")
        //大空スバルのチャンネルID(UC*****→UUに変更で取得可能)
        mChannelInfo.setChannelID("UUvzGlP9oQwU--Y0r9id_jnA")
        mChannelInfo.setPartPair("snippet")
        mChannelInfo.setMaxResult("50")
        mChannelInfo.getYoutubeParams(this.activity, root)
        return root
    }
}