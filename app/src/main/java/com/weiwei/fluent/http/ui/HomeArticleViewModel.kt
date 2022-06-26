package com.weiwei.fluent.http.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiwei.fluent.http.client.WanClient
import com.weiwei.fluent.http.doHttpFailure
import com.weiwei.fluent.http.doHttpSuccess
import com.weiwei.fluent.http.repository.bean.Article
import kotlinx.coroutines.launch

/**
 * @author weiwei
 * @date 2022.06.26
 */
class HomeArticleViewModel : ViewModel() {

  private val _articleListData: MutableLiveData<List<Article>> = MutableLiveData()
  val articleListData: LiveData<List<Article>> get() = _articleListData

  private val _articleListError: MutableLiveData<String> = MutableLiveData()
  val articleListError: LiveData<String> get() = _articleListError

  private val api = WanClient().getService()

  private var currentPage = 0

  init {
    loadArticleList(0)
  }

  fun loadNextArticleList() {
    loadArticleList(currentPage + 1)
  }

  private fun loadArticleList(page: Int) {
    currentPage = page

    viewModelScope.launch {
      val httpResult = api.getArticleList(page)
      httpResult.doHttpSuccess {
        val articleList = it.bodyData.responseData?.articleList ?: listOf()
        _articleListData.value = articleList
      }
      httpResult.doHttpFailure {
        _articleListError.value = it
      }
    }
  }
}