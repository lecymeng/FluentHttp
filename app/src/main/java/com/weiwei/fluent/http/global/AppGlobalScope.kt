@file:JvmName("AppGlobalScope")

package com.weiwei.fluent.http.global

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat

val mainHandler = Handler(Looper.getMainLooper())

val mainAsyncHandler = HandlerCompat.createAsync(Looper.getMainLooper())

inline val isMainThread: Boolean get() = (Looper.getMainLooper().thread === Thread.currentThread())

lateinit var mainContext: Context
  private set

lateinit var mainApplication: Application
  private set

fun initializeAppScope(application: Application) {
  mainContext = application.applicationContext
  mainApplication = application
}