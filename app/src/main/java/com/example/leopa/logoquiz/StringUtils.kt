package com.example.leopa.logoquiz

import java.text.Normalizer

object StringUtils {

    fun removeUmlauts(src: String): String {
        return Normalizer
            .normalize(src, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }

}