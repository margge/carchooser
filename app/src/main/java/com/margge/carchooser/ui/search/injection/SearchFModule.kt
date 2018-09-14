package com.margge.carchooser.ui.search.injection

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.margge.carchooser.ui.search.SearchViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SearchFModule(private val fragmentActivity: FragmentActivity) {

    @Singleton
    @Provides
    fun providesMainViewModel(): SearchViewModel =
            ViewModelProviders.of(fragmentActivity).get(SearchViewModel::class.java)
}