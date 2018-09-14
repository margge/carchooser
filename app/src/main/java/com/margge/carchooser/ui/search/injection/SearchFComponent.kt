package com.margge.carchooser.ui.search.injection

import com.margge.carchooser.ui.search.SearchActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(SearchFModule::class)])
interface SearchFComponent {

    fun inject(searchActivity: SearchActivity)
}