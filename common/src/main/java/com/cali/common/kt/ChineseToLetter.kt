package com.cali.common.kt


/**
 * 取得给定汉字串的首字母串,即声母串
 * @param str 给定汉字串
 * @return 声母串
 */
fun String?.getAllFirstLetter(): String {
    val str = this
    if (str == null || str.isEmpty()) {
        return ""
    }

    val strBuilder = StringBuilder()
    str.forEach {
        strBuilder.append(getFirstLetter(it.toString()))
    }
    return strBuilder.toString()
}

/**
 * 取得给定汉字的首字母,即声母
 * @param chinese 给定的汉字
 * @return 给定汉字的声母
 */
private fun getFirstLetter(chinese: String): String {
    var result = chinese
    if (result.isEmpty()) {
        return ""
    }
    result = conversionStr(result, "GB2312", "ISO8859-1")
    if (result.length > 1){
        // 判断是不是汉字
        val sectorCode = result[0].toInt() - 160// 汉字区码
        val positionCode = result[1].toInt()  - 160 // 汉字位码
        val secPosCode = sectorCode * 100 + positionCode // 汉字区位码
        if (secPosCode in 1601..5589) {
            for (i in 0..22) {
                if (secPosCode >= secPosValue[i] && secPosCode < secPosValue[i + 1]) {
                    result = firstLetter[i]
                    break
                }
            }
        }else{
            // 非汉字字符,如图形符号或ASCII码
            result = conversionStr(result, "ISO8859-1", "GB2312")
            result = result.substring(0, 1)
        }
    }
    return result
}

/**
 * 字符串编码转换
 * @param str 要转换编码的字符串
 * @param charsetName 原来的编码
 * @param toCharsetName 转换后的编码
 * @return 经过编码转换后的字符串
 */
private fun conversionStr(str: String, charsetName: String, toCharsetName: String): String {
    var result = str
    doWithTry {
        result = String(str.toByteArray(charset(charsetName)), charset(toCharsetName))
    }
    return result
}

private val secPosValue = intArrayOf(
    1601,
    1637,
    1833,
    2078,
    2274,
    2302,
    2433,
    2594,
    2787,
    3106,
    3212,
    3472,
    3635,
    3722,
    3730,
    3858,
    4027,
    4086,
    4390,
    4558,
    4684,
    4925,
    5249,
    5590
)
private val firstLetter = arrayOf(
    "a",
    "b",
    "c",
    "d",
    "e",
    "f",
    "g",
    "h",
    "j",
    "k",
    "l",
    "m",
    "n",
    "o",
    "p",
    "q",
    "r",
    "s",
    "t",
    "w",
    "x",
    "y",
    "z"
)