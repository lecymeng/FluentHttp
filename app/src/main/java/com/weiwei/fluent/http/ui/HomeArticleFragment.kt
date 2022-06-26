package com.weiwei.fluent.http.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.weiwei.fluent.http.databinding.FragmentHomeArticleBinding
import com.weiwei.fluent.http.repository.bean.Article

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeArticleFragment : Fragment() {

  private var _binding: FragmentHomeArticleBinding? = null

  // This property is only valid between onCreateView and onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentHomeArticleBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val multiTypeAdapter = MultiTypeAdapter()

    multiTypeAdapter.register(HomeArticleItemBinder())

    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    binding.recyclerView.adapter = multiTypeAdapter

    val list: ArrayList<Article> = ArrayList()
    val articleViewModel: HomeArticleViewModel by viewModels()
    articleViewModel.articleListData.observe(viewLifecycleOwner) {
      val itemCount = multiTypeAdapter.itemCount
      if (itemCount == 0) {
        list.addAll(it)
        multiTypeAdapter.items = list
        multiTypeAdapter.notifyDataSetChanged()
      } else {
        list.addAll(it)
        multiTypeAdapter.notifyItemRangeInserted(itemCount, it.size)
      }
    }
    articleViewModel.articleListError.observe(viewLifecycleOwner) {
      Toast.makeText(requireContext(), "Load home article error, please retry!", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}