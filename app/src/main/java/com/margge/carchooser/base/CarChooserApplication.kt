package com.margge.carchooser.base

import android.app.Application
import com.margge.carchooser.base.injection.CarChooserAppModule
import com.margge.carchooser.base.injection.DaggerCarChooserAppComponent
import com.margge.carchooser.helpers.SharedPrefHelper
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MANUFACTURER
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MODEL
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_YEAR
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_SELECTED_CAR
import com.margge.services.CarChooserApiService
import javax.inject.Inject

class CarChooserApplication : Application() {

    @Inject lateinit var carChooserApiService: CarChooserApiService
    @Inject lateinit var mSharedPrefHelper: SharedPrefHelper

    override fun onCreate() {
        super.onCreate()
        DaggerCarChooserAppComponent.builder()
                .carChooserAppModule(CarChooserAppModule(this))
                .build()
                .inject(this)

        mSharedPrefHelper.put(LAST_SELECTED_CAR, "")
        mSharedPrefHelper.put(LAST_CAR_MANUFACTURER, "")
        mSharedPrefHelper.put(LAST_CAR_MODEL, "")
        mSharedPrefHelper.put(LAST_CAR_YEAR, "")
    }
}