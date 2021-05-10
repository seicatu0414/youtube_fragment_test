package com.example.youtube_fragment_test

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*


class ChannelInfo {

    data class OneVideoInfo(
        val videoID: String,
        val description: String,
        val snippet_Title: String,
        val snippet_Thumbnails_Url: String
    )

    var mParams: MutableList<Pair<String, Any?>> = mutableListOf()
    var mChannelID: Pair<String, Any?> = Pair("playlistId", "UUeWZN7rNRQaHCtMCdHEZFqw")

    //ここにGoogle Cloud Platformで自作したプロジェクトのAPI_keyを入れる
    //「android_keyではなく、ブラウザキーとして作成する」
    var mApiKey: Pair<String, Any?> = Pair("key", "")
    var mPart: Pair<String, Any?> = Pair("part", "snippet")
    var mMaxResult: Pair<String, Any?> = Pair("maxResults", "50")

    var mOneVideoInfo: MutableList<OneVideoInfo> = mutableListOf()

    /**********************************************************************************************
     * description : 対象とするYoutubeチャンネルのIDのセット
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    fun setChannelID(ID: String) {
        mChannelID = Pair("playlistId", ID)
        searchList(mChannelID)

    }

    /**********************************************************************************************
     * description : 使用者のAPIKey（ブラウザ）のセット
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    fun setAPIKey(apiKey: String) {
        mApiKey = Pair("key", apiKey)
        searchList(mApiKey)
    }

    /**********************************************************************************************
     * description : YoutubeAPIのプロパティPartのセット（今回は使用しない）
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    fun setPartPair(part: String) {
        mPart = Pair("part", part)
        searchList(mPart)
    }

    /**********************************************************************************************
     * description : YoutubeAPIのプロパティmaxResultsのセット（MAX50）
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    fun setMaxResult(max: String) {
        mMaxResult = Pair("maxResults", max)
        searchList(mMaxResult)
    }

    /**********************************************************************************************
     * description : JSON上のKeyの更新、追加
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    private fun searchList(pair: Pair<String, Any?>) {
        var flg = false
        for (i in 0 until mParams.size) {
            if (mParams[i].first == pair.first) {
                mParams[i] = pair
                flg = true
            }
        }
        if (!flg) {
            mParams.add(pair)
        }
    }

    /**********************************************************************************************
     * description : https通信によるYoutubeChannel情報の取得
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    fun getYoutubeParams(activity: FragmentActivity?, view: View) {
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            // JSONデータを取得する
            val jsonData = jsonGET("https://www.googleapis.com/youtube/v3/playlistItems")
            val root: JsonObject = Gson().fromJson(jsonData, JsonObject::class.java)
            jsonSnippetsParse(root)
            setAdapter(activity, view)

        }
    }

    /**********************************************************************************************
     * description : 各フラグメントアクティビティにアダプタをセットする
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/04
     **********************************************************************************************/
    private fun setAdapter(activity: FragmentActivity?, view: View) {
        var channelContentsRyView: RecyclerView? = null
        if (view.tag.toString() == "ぺこら") {
            channelContentsRyView =
                view.findViewById(R.id.channelPekoraContentsRecyclerView) as RecyclerView
        } else if (view.tag.toString() == "みこ") {
            channelContentsRyView =
                view.findViewById(R.id.channelMikochiContentsRecyclerView) as RecyclerView
        } else if (view.tag.toString() == "すばる") {
            channelContentsRyView =
                view.findViewById(R.id.channelSubaruContentsRecyclerView) as RecyclerView
        }

        val adapter = ContentsRecyclerAdapter(mOneVideoInfo)
        val layoutManager = LinearLayoutManager(activity)
        channelContentsRyView!!.layoutManager = layoutManager
        channelContentsRyView!!.adapter = adapter
        channelContentsRyView!!.setHasFixedSize(true)
    }

    /**********************************************************************************************
     * description : JSON情報の取得
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    private suspend fun jsonGET(url: String): String? = GlobalScope.async {
        val triple = url.httpGet(mParams).response()
        return@async String(triple.second.data)
    }.await()

    /**********************************************************************************************
     * description : 取得したJSON情報の解析
     * created by  :  kohei yamaguchi
     * created at  : 2021/05/03
     **********************************************************************************************/
    private fun jsonSnippetsParse(jsonData: JsonObject) {
        //json内itemsの取得
        val itemsObjArray: JsonArray = jsonData.get("items").asJsonArray
        for (i in 0 until itemsObjArray.size()) {
            val oneItem: JsonObject = itemsObjArray.get(i).asJsonObject
            val oneItemSnippet: JsonObject = oneItem.get("snippet").asJsonObject
            //タイトルの取得
            val oneItemSnippetTitle: String = oneItemSnippet.get("title").asString
            print(oneItemSnippetTitle)
            //動画内容の取得（使うかは不明）
            val oneItemSnippetDescription: String = oneItemSnippet.get("description").asString
            print(oneItemSnippetDescription)
            //サムネイル画像URLの取得
            val oneItemSnippetThumbnails: JsonObject = oneItemSnippet.get("thumbnails").asJsonObject
            val oneItemSnippetThumbnailsDefault: JsonObject =
                oneItemSnippetThumbnails.get("standard").asJsonObject
            val oneItemThumbnailsDefaultUrl: String =
                oneItemSnippetThumbnailsDefault.get("url").asString
            print(oneItemThumbnailsDefaultUrl)
            //VideoIDの取得
            val oneItemSnippetResourceId: JsonObject = oneItemSnippet.get("resourceId").asJsonObject
            val oneItemSnippetResourceIdVideoUrl: String =
                oneItemSnippetResourceId.get("videoId").asString
            print(oneItemSnippetResourceIdVideoUrl)
            mOneVideoInfo.add(
                OneVideoInfo(
                    oneItemSnippetResourceIdVideoUrl,
                    oneItemSnippetDescription,
                    oneItemSnippetTitle,
                    oneItemThumbnailsDefaultUrl
                )
            )
        }
    }


}