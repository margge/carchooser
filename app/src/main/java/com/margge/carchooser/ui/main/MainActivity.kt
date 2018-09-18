package com.margge.carchooser.ui.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.margge.carchooser.Event
import com.margge.carchooser.R
import com.margge.carchooser.databinding.ActivityMainBinding
import com.margge.carchooser.extensions.showToast
import com.margge.carchooser.ui.main.MainViewModel.Companion.DATA
import com.margge.carchooser.ui.main.injection.DaggerMainFComponent
import com.margge.carchooser.ui.main.injection.MainFModule
import com.margge.carchooser.ui.search.SearchActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMainFComponent.builder()
                .mainFModule(MainFModule(this@MainActivity))
                .build()
                .inject(this@MainActivity)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel

        subscribeViewModelEvents()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.refreshRecentSearches()
    }

    override fun onDestroy() {
        mainViewModel.mainViewEvents.removeObservers(this)
        super.onDestroy()
    }

    private fun subscribeViewModelEvents() {
        mainViewModel.mainViewEvents.observe(this, Observer<Event> { event: Event? ->

            when (event?.name) {

                Event.EventsType.GetData -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.putExtra(DATA, event.data as String)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

                Event.EventsType.ConnectionError ->
                    showToast(getString(R.string.server_error))
            }
        })
    }
}
