package com.margge.carchooser.ui.main.injection

import com.margge.carchooser.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(MainFModule::class)])
interface MainFComponent {

    fun inject(mainActivity: MainActivity)
}