package com.weiwei.fluent.http.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author weiwei
 * @date 2022.05.30
 */
object NetworkUtils {
  /**
   * 网络是否可用
   */
  @Suppress("DEPRECATION")
  fun isNetworkAvailable(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connMgr.activeNetworkInfo?.isAvailable == true
  }

  /**
   * 是否连接Wifi
   */
  @Suppress("DEPRECATION")
  fun isWifi(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connMgr.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
  }
}