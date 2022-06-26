package com.weiwei.fluent.http.api

import com.weiwei.fluent.http.HttpResult
import com.weiwei.fluent.http.repository.bean.HomeArticle
import com.weiwei.fluent.http.repository.bean.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author weiwei
 * @date 2022.06.26
 */
interface WanApi {
  @GET("/article/list/{page}/json")
  suspend fun getArticleList(@Path("page") page: Int): HttpResult<WanResponse<HomeArticle>>
}