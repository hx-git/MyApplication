package com.cali.common.kt

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import com.muddzdev.styleabletoast.StyleableToast
import com.tbruyelle.rxpermissions2.RxPermissions
import me.yokeyword.fragmentation.SupportActivity


fun Context.toast(tip:String) {
//    Toast.makeText(this,tip,Toast.LENGTH_SHORT).show()
    StyleableToast.Builder(this)
        .text(tip)
        .gravity(Gravity.CENTER)
        .textColor(Color.WHITE)
        .backgroundColor(Color.GRAY)
        .show()
}

fun Context.format(name:Int, vararg items:String): String {
    //resources.getIdentifier(name,"string",this.packageName)
    return resources.getString(name, *items)
}

fun Context.format(name:Int, vararg items:Any): String {
    //resources.getIdentifier(name,"string",this.packageName)
    return resources.getString(name, *items)
}

fun Context.versionCode(): Int{
    return if (Build.VERSION.SDK_INT >= 28) {
        packageInfo()?.longVersionCode?.toInt()?:1
    }else{
        packageInfo()?.versionCode?:1
    }

}

fun Context.packageInfo(): PackageInfo? {
    val manager = packageManager
    return manager.getPackageInfo(packageName,0)
}




fun Activity?.saveToPhone(bitmap: Bitmap, name: String) {
    if (this !is SupportActivity) return
    val rxPermissions = RxPermissions(this)
    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val subscribe = rxPermissions.request(*permissions)
        .subscribe {
            if (it == true) {
                //保存bitmap
                bitmap.saveBitmapToMedia(this,name)
            } else {
                this.toast("需要存储权限")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                finish()
            }
        }
}
