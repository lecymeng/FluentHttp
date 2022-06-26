package com.weiwei.fluent.http.repository.bean

import com.google.gson.annotations.SerializedName

/**
 * @author weiwei
 * @date 2022.06.26
 */
data class WanResponse<T>(
  @SerializedName("data")
  var responseData: T? = null,
  @SerializedName("errorCode")
  var errorCode: Int = 0,
  @SerializedName("errorMsg")
  var errorMsg: String = ""
)