package com.margge.carchooser.base.injection

import com.margge.carchooser.base.CarChooserApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(CarChooserAppModule::class)])
interface CarChooserAppComponent {

    fun inject(application:CarChooserApplication)
}