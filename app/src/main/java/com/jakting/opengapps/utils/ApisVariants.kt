package com.jakting.opengapps.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jakting.opengapps.R
import com.jakting.opengapps.utils.bean.VariantBean


class ApisVariants {
    companion object {
        fun Apis(context: Context, apis: JsonObject): ArrayList<String?> {
            var apisArray = ArrayList<String?>()
            var parser = JsonParser()
            var it: Iterator<*> = apis.entrySet().iterator()
            val plz: String = context.getString(R.string.text_download_pls)
            apisArray.add(plz)
            while (it.hasNext()) {
                val entry =
                    it.next() as Map.Entry<*, *>
                apisArray.add("" + entry.key)
            }
            return apisArray
        }

        fun Variants(context: Context, apis: JsonObject, versionS: String?): ArrayList<String?> {
            val variantsArray = ArrayList<String?>()
            var versionSS = versionS
            if (versionSS!!.contains("R") || versionSS.contains("11")) {
                //OpenGApps 尚未支持的版本，不能自动获取：Android R/11
                versionSS = "10.0"
            }
            if(apis[versionSS]==null){
                val unsupported: String = context.getString(R.string.text_device_os_unsupported)
                variantsArray.add(unsupported)
            }else{
                val version = apis[versionSS].asJsonObject
                val variants = version["variants"].asJsonArray
                val gson = Gson()
                val variantBeanArrayList: ArrayList<VariantBean> = ArrayList()
                //加强for循环遍历JsonArray
                for (user in variants) { //使用GSON，直接转成Bean对象
                    val variantBean = gson.fromJson(user, VariantBean::class.java)
                    variantBeanArrayList.add(variantBean)
                }
                val plz: String = context.getString(R.string.text_download_pls)
                variantsArray.add(plz)
                var variantBean: VariantBean
                for (i in 0 until variantBeanArrayList.size) {
                    variantBean = variantBeanArrayList[i]
                    //Log.d("debug", "Variants: ||||||||||||||||||||||||||"+variantBean.name);
                    variantsArray.add(variantBean.name)
                }
            }

            return variantsArray
        }
    }
}