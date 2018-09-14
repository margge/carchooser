package com.margge.carchooser.base

import android.app.Application
import com.margge.carchooser.base.injection.CarChooserAppModule
import com.margge.carchooser.base.injection.DaggerCarChooserAppComponent
import com.margge.services.CarChooserApiService
import javax.inject.Inject

class CarChooserApplication : Application() {

    @Inject lateinit var carChooserApiService: CarChooserApiService

    override fun onCreate() {
        super.onCreate()
        DaggerCarChooserAppComponent.builder()
                .carChooserAppModule(CarChooserAppModule())
                .build()
                .inject(this)
    }
}