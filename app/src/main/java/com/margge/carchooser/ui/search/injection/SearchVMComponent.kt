package com.margge.carchooser.ui.search.injection

import com.margge.carchooser.ui.search.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(SearchVMModule::class)])
interface SearchVMComponent {

    fun inject(searchViewModel: SearchViewModel)
}