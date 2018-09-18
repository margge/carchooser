package com.margge.carchooser.ui.search

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.animation.AnimationUtils
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

    @Inject lateinit var searchViewModel: SearchViewModel

    private val dataAdapter by lazy { DataAdapter(this, searchViewModel.searchViewEvents) }
    private val dataLayoutManager by lazy { LinearLayoutManager(this) }
    private val dataType by lazy { intent.getStringExtra(DATA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        DaggerSearchFComponent.builder()
                .searchFModule(SearchFModule(this@SearchActivity))
                .build()
                .inject(this@SearchActivity)

        val binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        binding.searchViewModel = searchViewModel

        searchEditText.setOnQueryTextListener(this)

        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)

        dataRecyclerView.apply {
            layoutManager = dataLayoutManager
            adapter = dataAdapter
            layoutAnimation = animation
        }

        searchViewModel.getData(dataType)
        subscribeViewModelEvents()
    }

    override fun onDestroy() {
        searchViewModel.searchViewEvents.removeObservers(this)
        super.onDestroy()
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        dataAdapter.filter.filter(query)
        return true
    }

    private fun subscribeViewModelEvents() {
        searchViewModel.searchViewEvents.observe(this, Observer<Event> { event: Event? ->

            when (event?.name) {

                Event.EventsType.DataLoaded -> {
                    dataAdapter.addAll(event.data as List<Pair<String, String>>)
                    runLayoutAnimation()
                }

                Event.EventsType.SelectedItem -> {
                    val selectedData: Pair<String, String> = event.data as Pair<String, String>
                    searchViewModel.saveSelectedData(dataType, selectedData)
                    launchActivity<MainActivity>()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

                Event.EventsType.ConnectionError ->
                    showToast(getString(R.string.server_error))
            }
        })
    }

    private fun runLayoutAnimation() {
        val context = dataRecyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        dataRecyclerView.apply {
            layoutAnimation = controller
            adapter.notifyDataSetChanged()
            scheduleLayoutAnimation()
        }
    }
}