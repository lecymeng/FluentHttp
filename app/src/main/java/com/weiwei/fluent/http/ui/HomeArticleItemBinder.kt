package com.weiwei.fluent.http.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.weiwei.fluent.http.R
import com.weiwei.fluent.http.repository.bean.Article

/**
 * @author weiwei
 * @date 2022.06.26
 */
class HomeArticleItemBinder : ItemViewBinder<Article, HomeArticleItemBinder.ArticleViewHolder>() {
  class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
  }

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ArticleViewHolder {
    return ArticleViewHolder(inflater.inflate(R.layout.item_home_article, parent, false))
  }

  override fun onBindViewHolder(holder: ArticleViewHolder, item: Article) {
    holder.tvTitle.text = item.title
    holder.tvDescription.text = item.desc
  }
}