package com.example.youtube_fragment_test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.youtube_fragment_test.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YoutubePlayerSupportFragmentX


class YoutubeFragment : Fragment() {

    var mVideoId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_yotube_player, container, false)
        // YouTubeフラグメントインスタンスを取得
        var youTubePlayerFragment = YoutubePlayerSupportFragmentX.newInstance()

        // レイアウトにYouTubeフラグメントを追加
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.youtube_holder, youTubePlayerFragment).commit()


        // YouTubeフラグメントのプレーヤーを初期化する
        youTubePlayerFragment.initialize(mVideoId, object : YouTubePlayer.OnInitializedListener {
            // YouTubeプレーヤーの初期化成功
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player?.cueVideo(mVideoId)
                }
            }

            // YouTubeプレーヤーの初期化失敗
            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                error: YouTubeInitializationResult
            ) {
                // YouTube error
                val errorMessage = error.toString()
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
        return rootView
    }
}
