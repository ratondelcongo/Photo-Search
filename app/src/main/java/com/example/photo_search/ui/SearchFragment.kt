package com.example.photo_search.ui

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter

class SearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {

    interface OnSomeEventListener {
        fun someEvent(query: String?)
    }

    private var someEventListener: OnSomeEventListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as OnSomeEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnSomeEventListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchResultProvider(this)

    }

    override fun getResultsAdapter(): ObjectAdapter {
        return ArrayObjectAdapter(ListRowPresenter())
    }

    override fun onQueryTextChange(newQuery: String): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (!TextUtils.isEmpty(query)) {
            someEventListener?.someEvent(query);
        }
        return true
    }
}
    