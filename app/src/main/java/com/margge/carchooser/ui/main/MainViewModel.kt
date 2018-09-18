package com.margge.carchooser.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.margge.carchooser.Event
import com.margge.carchooser.base.CarChooserApplication
import com.margge.carchooser.helpers.SharedPrefHelper
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MANUFACTURER
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MODEL
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_SEARCHES
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_SELECTED_CAR
import com.margge.carchooser.ui.main.injection.DaggerMainVMComponent
import com.margge.carchooser.ui.main.injection.MainVMModule
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DATA = "DATA"
        const val CAR_MANUFACTURER = "CAR_MANUFACTURER"
        const val CAR_MODEL = "CAR_MODEL"
        const val CAR_YEAR = "CAR_YEAR"
    }

    @Inject lateinit var mSharedPrefHelper: SharedPrefHelper

    val mMainViewEvents: MutableLiveData<Event> = MutableLiveData()
    val mLastSelectedCar = ObservableField("")
    val mLastSearches = ObservableField("")

    init {
        DaggerMainVMComponent.builder()
                .mainVMModule(MainVMModule(application as CarChooserApplication))
                .build()
                .inject(this@MainViewModel)
    }

    fun getCarManufacturers() =
            mMainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_MANUFACTURER))

    fun getCarModels() {
        if (mSharedPrefHelper[LAST_CAR_MANUFACTURER, ""].isEmpty())
            mMainViewEvents.postValue(Event(Event.EventsType.MissingValue, CAR_MANUFACTURER))
        else
            mMainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_MODEL))
    }

    fun getCarYears() {
        when {
            mSharedPrefHelper[LAST_CAR_MANUFACTURER, ""].isEmpty() ->
                mMainViewEvents.postValue(Event(Event.EventsType.MissingValue, CAR_MANUFACTURER))
            mSharedPrefHelper[LAST_CAR_MODEL, ""].isEmpty() ->
                mMainViewEvents.postValue(Event(Event.EventsType.MissingValue, CAR_MODEL))
            else ->
                mMainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_YEAR))
        }
    }

    fun refreshRecentSearches() {
        mLastSearches.set(mSharedPrefHelper[LAST_SEARCHES, ""])
        mLastSelectedCar.set(mSharedPrefHelper[LAST_SELECTED_CAR, ""])
    }
}