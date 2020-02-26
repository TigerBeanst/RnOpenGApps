package com.jakting.opengapps.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class GetFilename {
    companion object {
        fun get(url: String): String? {
            val regex =
                "https:\\/\\/downloads\\.sourceforge\\.net\\/project\\/opengapps\\/.*?\\/.*?\\/(.*?)\\?r";
            val pattern: Pattern = Pattern.compile(regex, Pattern.MULTILINE)
            val matcher: Matcher = pattern.matcher(url)
            var filename:String? = ""
            while (matcher.find()) {
                filename = matcher.group(1)
            }
            return filename
        }
    }
}