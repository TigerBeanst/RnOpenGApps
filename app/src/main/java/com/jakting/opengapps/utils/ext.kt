package com.jakting.opengapps.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

val OPENGAPPS_API = "https://api.opengapps.org/list"

fun Context?.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context?.longtoast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun logd(message: String) =
    Log.d("debug", message)

fun getCPUObject(json: String, cpuShort: String): JsonObject {
    val obj: JsonObject = JsonParser().parse(json).asJsonObject
    val archs = obj["archs"].asJsonObject
    return archs[cpuShort].asJsonObject
    //val date = cpu_chip.get("date").asString
}

fun getApisObject(cpu_chip: JsonObject): JsonObject {
    return cpu_chip["apis"].asJsonObject
}

fun getApisList(context: Context, apis: JsonObject): ArrayList<String?> {
    return ApisVariants.Apis(context, apis)
}

fun getVariantsList(context: Context, apis: JsonObject, version: String): ArrayList<String?> {
    return ApisVariants.Variants(context, apis, version)

}

fun humanReadableBytes(bytes: Long, si: Boolean): String? {
    /*
    来自：lingochamp/okdownload
    地址：https://github.com/lingochamp/okdownload/blob/master/okdownload/src/main/java/com/liulishuo/okdownload/core/Util.java
     */
    val unit = if (si) 1000 else 1024
    if (bytes < unit) return "$bytes B"
    val exp =
        (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre =
        (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
    return java.lang.String.format(
        Locale.ENGLISH,
        "%.1f %sB",
        bytes / Math.pow(unit.toDouble(), exp.toDouble()),
        pre
    )
}


