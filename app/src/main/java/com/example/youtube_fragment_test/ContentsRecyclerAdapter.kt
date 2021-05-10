package com.example.youtube_fragment_test

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtube_fragment_test.ui.YoutubeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ContentsRecyclerAdapter(private val videoInfoList: MutableList<ChannelInfo.OneVideoInfo>) :
    RecyclerView.Adapter<ContentsRecyclerAdapter.CustomViewHolder>() {

    private var mYoutubeVideos: MutableList<ChannelInfo.OneVideoInfo> = videoInfoList

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var thumbnailView: ImageView = view.findViewById(R.id.thumbnail_view)
        var titleView: TextView = view.findViewById(R.id.title_view)

        //動画内容取得（題材の動画詳細内容が長すぎるのでいいUIが思い浮かばない為今回は未使用）
        // var descriptionView: TextView = view.findViewById(R.id.description_view)
        var oneContentsView = view
    }


    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.contents_recycle_items, parent, false)
        return CustomViewHolder(item)
    }

    fun setItems(youtubeVideos: MutableList<ChannelInfo.OneVideoInfo>) {
        mYoutubeVideos = youtubeVideos
        notifyDataSetChanged()
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int {
        return videoInfoList.size
    }


    // ViewHolderに表示する画像とテキストを挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = Glide.with(holder.thumbnailView)
                .asBitmap()
                .load(mYoutubeVideos[position].snippet_Thumbnails_Url)
                .submit()
                .get()
            Handler(Looper.getMainLooper()).postDelayed({
                holder.thumbnailView.setImageBitmap(bitmap)
            }, 500)
        }
        //kokoniif(frag == 0)deibennto回避？
        holder.oneContentsView.setOnClickListener { v ->
            val activity = v!!.context as MainActivity
            val ytFragment = YoutubeFragment()
            //ロードするVideoIDの受け渡し
            ytFragment.mVideoId = mYoutubeVideos[position].videoID
            val frag = activity.supportFragmentManager.backStackEntryCount
            if (frag == 0) {
                //MainActivityのsupportFragmentManagerにytFragmentを追加
                activity.supportFragmentManager.beginTransaction()
                    .add(R.id.container, ytFragment, "ytFragment").addToBackStack(null).commit()
            } else {
                //複数のYoutubeFragmentが重複するとバグが起きるためスタックが積まれているときは削除して追加20210506
                val beforeYtFragment =
                    activity.supportFragmentManager.findFragmentByTag("ytFragment")
                activity.supportFragmentManager.beginTransaction().remove(beforeYtFragment!!)
                    .commit()
                activity.supportFragmentManager.beginTransaction()
                    .add(R.id.container, ytFragment, "ytFragment").addToBackStack(null).commit()
            }

        }
        holder.titleView.text = mYoutubeVideos[position].snippet_Title
    }
}