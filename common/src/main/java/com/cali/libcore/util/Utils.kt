package com.cali.libcore.util


import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import java.security.MessageDigest
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

object Utils {
    fun randomString(length: Int): String {
        val base = "abcdefghijklmnopqrstuvwxyz0123456789" //生成字符串从此序列中取
        val random = Random()
        val randomStr = StringBuilder()
        for (i in 0 until length) {
            val number = random.nextInt(36)
            randomStr.append(base[number])
        }
        return randomStr.toString()
    }

    enum class HashAlgorithm {
        MD5, SHA1, SHA256, SHA384, SHA512
    }

    //MD5,SHA-1,SHA-256,SHA-384,SHA-512
    internal fun getHash(data: String, hashType: HashAlgorithm): String {
        return getHash(data.toByteArray(), hashType)
    }

    private fun getHash(data: ByteArray, hashAlgorithm: HashAlgorithm): String {
        val hashType = when (hashAlgorithm) {
            HashAlgorithm.MD5 -> "MD5"
            HashAlgorithm.SHA1 -> "SHA-1"
            HashAlgorithm.SHA256 -> "SHA-256"
            HashAlgorithm.SHA384 -> "SHA-384"
            HashAlgorithm.SHA512 -> "SHA-512"
        }
        return try {
            val md5 = MessageDigest.getInstance(hashType)
            md5.update(data)
            toHexString(md5.digest())
        } catch (e: Exception) {
            ""
        }

    }

    //HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
    internal fun getHmacHash(data: String, key: String, hashAlgorithm: HashAlgorithm): String {
        return getHmacHash(data.toByteArray(), key.toByteArray(), hashAlgorithm)
    }

    private fun getHmacHash(data: ByteArray, key: ByteArray, hashAlgorithm: HashAlgorithm): String {
        val hashType = when (hashAlgorithm) {
            HashAlgorithm.MD5 -> "HmacMD5"
            HashAlgorithm.SHA1 -> "HmacSHA1"
            HashAlgorithm.SHA256 -> "HmacSHA256"
            HashAlgorithm.SHA384 -> "HmacSHA384"
            HashAlgorithm.SHA512 -> "HmacSHA512"
        }
        return try {
            val signingKey = SecretKeySpec(key, hashType)
            val mac = Mac.getInstance(hashType)
            mac.init(signingKey)
            val rawHmac = mac.doFinal(data)
            toHexString(rawHmac)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

    private fun toHexString(b: ByteArray): String {
        val hexChar = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val sb = StringBuilder(b.size * 2)
        for (aB in b) {
            sb.append(hexChar[(aB and 0xf0.toByte()).toInt().ushr(4)])
            sb.append(hexChar[(aB and 0x0f).toInt()])
        }
        return sb.toString()
    }

    internal fun isAppInstalled(context: Context, packageName: String): Boolean {
        try {
            context.packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    //    /**
    //     * 这里开始计时操作?
    //     */
    //    public static void getBeganStart(BaseActivity mActivity, String adId) {
    //        HashMap<String, String> paramsMap = new HashMap<>();
    //        paramsMap.put(ApiStartReadAd.FROM_UID, mActivity.getUserBean().getUserid());
    //        paramsMap.put(ApiStartReadAd.ADID, adId);
    //        paramsMap.put(ApiStartReadAd.TYPE, "90");
    //        ApiStartReadAd apiStartReadAd = new ApiStartReadAd(new HttpOnNextListener<ResEmpty>() {
    //
    //            @Override
    //            public void onNext(ResEmpty resEmpty) {
    ////                mActivity.requestStartCountDown();
    //                getReadCoin(mActivity, adId);
    //            }
    //
    //            @Override
    //            public void onError(Throwable e) {
    //                super.onError(e);
    //                if (e instanceof HttpTimeException) {
    //                    HttpTimeException exception = (HttpTimeException) e;
    //                    //这里表示已经领取过该任务了
    //                    Toast toast = Toast.makeText(mActivity, exception.getMessage(), Toast.LENGTH_SHORT);
    //                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
    //                    v.setTextSize(20);
    //                    toast.show();
    ////                   mActivity.hideReadProgress();
    //                }
    //            }
    //        }, mActivity, paramsMap);
    //        HttpManager.getInstance().doHttpDeal(apiStartReadAd);
    //    }
    //
    //    /**
    //     * 结束计时操作
    //     */
    //    public static void getReadCoin(BaseActivity mActivity, String adId) {
    //        HashMap<String, String> paramsMap = new HashMap<>();
    //        paramsMap.put(ApiEndReadAd.FROM_UID, mActivity.getUserBean().getUserid());
    //        paramsMap.put(ApiEndReadAd.ADID, adId);
    //        paramsMap.put(ApiStartReadAd.TYPE, "90");
    //        ApiEndReadAd apiEndReadAd = new ApiEndReadAd(new HttpOnNextListener<ResReadAwarad>() {
    //            @Override
    //            public void onNext(ResReadAwarad resReadNewsAward) {
    //                Log.i("info", "ApiEndReadAd=" + resReadNewsAward.toString());
    //                //这里要更新进度；
    //                SharedPreferencesUtils.getInstance(mActivity).setReward(mActivity
    //                        , resReadNewsAward.getLastcount_new());
    //                SharedPreferencesUtils.getInstance(mActivity).setRewardNum(mActivity,
    //                        resReadNewsAward.getIntegral() + "");
    //            }
    //
    //            @Override
    //            public void onError(Throwable e) {
    //                super.onError(e);
    //            }
    //        }, mActivity, paramsMap);
    //        HttpManager.getInstance().doHttpDeal(apiEndReadAd);
    //    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) {
            return false
        }
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn.className) {
                return true
            }
        }
        return false
    }

    fun closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT >= 28) {

            try {
                val aClass = Class.forName("android.content.pm.PackageParser\$Package")
                val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
                declaredConstructor.isAccessible = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val cls = Class.forName("android.app.ActivityThread")
                val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
                declaredMethod.isAccessible = true
                val activityThread = declaredMethod.invoke(null)
                val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
                mHiddenApiWarningShown.isAccessible = true
                mHiddenApiWarningShown.setBoolean(activityThread, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
