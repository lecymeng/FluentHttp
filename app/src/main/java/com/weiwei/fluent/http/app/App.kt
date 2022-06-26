package com.weiwei.fluent.http.app

import android.app.Application
import com.weiwei.fluent.http.global.initializeAppScope

/**
 * @author weiwei
 * @date 2022.06.26
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()

    initializeAppScope(this)
  }
}