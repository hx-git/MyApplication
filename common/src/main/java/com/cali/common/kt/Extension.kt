package com.cali.common.kt

import com.cali.common.constant.DEFAULT_PAGE_SIZE
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern
import kotlin.random.Random


//校验数字至少一位
fun String.isNum(): Boolean {
//    ^\d{n,}$
    val pattern = "^\\d+\$"
    val matcher = Pattern.compile(pattern).matcher(this)
    return matcher.matches()
}
 inline fun <T : Any?> ktScheduler(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doFinally {

                }
    }
}

// 2 fileName



/**
 * 必须使用等于
 * 保证每个页调用一次
 *
 * page参数用于 去除往回滑的情况
 */
fun Int.judge(page:Int): Boolean {
    val size = DEFAULT_PAGE_SIZE.toInt()
    return this % size == size -3  &&  this/size == page -1
}

/**
 * 判断是否需要请求新数据
 */
fun Int.needLoadMore():Boolean {
    val size = DEFAULT_PAGE_SIZE.toInt()
    return this % size >= size - 3
}



fun <T : Number> String.parseNum(t: Class<T>): T {
    return when (t) {
        Float::class.java -> {
            val doWithTryReturn = doWithTryReturn {
                this.toFloat()
            }
            if(doWithTryReturn) this.toFloat() as T  else 0.0 as T
        }
        Int::class.java -> {
            val doWithTryReturn = doWithTryReturn {
                this.toInt()
            }
            if(doWithTryReturn) this.toInt() as T  else 0 as T
        }
        else -> 0 as T
    }
}




//随机数
fun generateCode(): String {
    val str = "0123456789"
    val sb = StringBuilder(4)
    repeat(4) {
        val ch = str.toCharArray()[Random.nextInt(str.length)]
        sb.append(ch)
    }
    return sb.toString()
}

//jkb随机兑换金额 标准30 - 0.18
//范围 0.15 - 0.21
fun generateJkbMoney(): String {
    val nextInt = Random.nextInt(15, 21)
    return (nextInt.toFloat()/100).toString()
}

//随机数

fun Int.generateInt(): Int {
    return Random.nextInt(0,this)
}




