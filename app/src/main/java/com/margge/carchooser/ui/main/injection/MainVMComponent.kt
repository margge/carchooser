package com.margge.carchooser.ui.main.injection

import com.margge.carchooser.ui.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(MainVMModule::class)])
interface MainVMComponent {

    fun inject(mainViewModel: MainViewModel)
}