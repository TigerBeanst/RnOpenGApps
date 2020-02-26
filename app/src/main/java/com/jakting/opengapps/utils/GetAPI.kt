package com.jakting.opengapps.utils

import android.os.Handler
import android.os.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


val RETURN_JSON = 1
val RETURN_MD5 = 2

class GetAPI(var myHandler: Handler) {
    private var client = OkHttpClient()
    var json: String = ""
    @Throws(IOException::class)
    fun run(url: String) {
        val request: Request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).execute().use { response ->
            json = response.body!!.string()
            //logd("JSON:////:$json")
            val msg = Message.obtain()
            msg.what = if (url.contains("md5")) RETURN_MD5 else RETURN_JSON
            myHandler.sendMessage(msg)

        }
    }

}