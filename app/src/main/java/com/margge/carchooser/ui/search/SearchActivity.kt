package com.margge.carchooser.ui.search

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.SearchView
import com.margge.carchooser.Event
import com.margge.carchooser.R
import com.margge.carchooser.databinding.ActivitySearchBinding
import com.margge.carchooser.extensions.launchActivity
import com.margge.carchooser.extensions.showToast
import com.margge.carchooser.ui.main.MainActivity
import com.margge.carchooser.ui.main.MainViewModel.Companion.DATA
import com.margge.carchooser.ui.search.adapter.DataAdapter
import com.margge.carchooser.ui.search.injection.DaggerSearchFComponent
import com.margge.carchooser.ui.search.injection.SearchFModule
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject lateinit var mSearchViewModel: SearchViewModel

    private val mDataAdapter by lazy { DataAdapter(this, mSearchViewModel.mSearchViewEvents) }
    private val mDataLayoutManager by lazy { LinearLayoutManager(this) }
    private val mDataType by lazy { intent.getStringExtra(DATA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        DaggerSearchFComponent.builder()
                .searchFModule(SearchFModule(this@SearchActivity))
                .build()
                .inject(this@SearchActivity)

        val binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        binding.searchViewModel = mSearchViewModel

        searchEditText.setOnQueryTextListener(this)
        dataRecyclerView.apply {
            layoutManager = mDataLayoutManager
            adapter = mDataAdapter
        }

        mSearchViewModel.getData(mDataType)
        subscribeViewModelEvents()
    }

    override fun onDestroy() {
        mSearchViewModel.mSearchViewEvents.removeObservers(this)
        super.onDestroy()
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        mDataAdapter.filter.filter(query)
        return true
    }

    private fun subscribeViewModelEvents() {
        mSearchViewModel.mSearchViewEvents.observe(this, Observer<Event> { event: Event? ->

            when (event?.name) {

                Event.EventsType.DataLoaded ->
                    mDataAdapter.addAll(event.data as List<Pair<String, String>>)

                Event.EventsType.SelectedItem -> {
                    val selectedData: Pair<String, String> = event.data as Pair<String, String>
                    mSearchViewModel.saveSelectedData(mDataType, selectedData)
                    launchActivity(MainActivity::class.java)
                }

                Event.EventsType.ConnectionError -> showToast(getString(R.string.server_error))
            }
        })
    }
}