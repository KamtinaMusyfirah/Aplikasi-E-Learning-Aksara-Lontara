package com.example.aplikasie_learning.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.aplikasie_learning.databinding.ItemPageMateriBinding
import com.example.aplikasie_learning.model.Page
import com.example.aplikasie_learning.model.PartsPage

class PagesAdapter(private val context: Context): PagerAdapter() {

    var pages = mutableListOf<Page>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int = pages.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pageBinding = ItemPageMateriBinding.inflate(LayoutInflater.from(context), container, false)

        bindItem(pageBinding, pages[position])
        container.addView(pageBinding.root)
        return pageBinding.root
    }

    private fun bindItem(pageBinding: ItemPageMateriBinding, page: Page){
        val partsPageAdapter = PartsPageAdapter()

        partsPageAdapter.partsPage = page.partsPage as MutableList<PartsPage>

        pageBinding.rvPages.setHasFixedSize(true)
        pageBinding.rvPages.adapter = partsPageAdapter
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as View)

}