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

    @Inject lateinit var sharedPrefHelper: SharedPrefHelper

    val mainViewEvents: MutableLiveData<Event> = MutableLiveData()
    val lastSelectedCar = ObservableField("")
    val lastSearches = ObservableField("")
    val visibleModel = ObservableField(false)
    val visibleYear = ObservableField(false)
    val visibleSelectedCar = ObservableField(false)
    val visiblePreviousSearches = ObservableField(false)

    init {
        DaggerMainVMComponent.builder()
                .mainVMModule(MainVMModule(application as CarChooserApplication))
                .build()
                .inject(this@MainViewModel)
    }

    fun getCarManufacturers() =
            mainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_MANUFACTURER))

    fun getCarModels() =
            mainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_MODEL))

    fun getCarYears() =
            mainViewEvents.postValue(Event(Event.EventsType.GetData, CAR_YEAR))


    fun refreshRecentSearches() {
        visibleModel.set(sharedPrefHelper[LAST_CAR_MANUFACTURER, ""].isNotEmpty())
        visibleYear.set(sharedPrefHelper[LAST_CAR_MODEL, ""].isNotEmpty())

        val lastSearches = sharedPrefHelper[LAST_SEARCHES, ""]
        this.lastSearches.set(lastSearches)
        visiblePreviousSearches.set(lastSearches.isNotEmpty())

        val lastSelectedCar = sharedPrefHelper[LAST_SELECTED_CAR, ""]
        this.lastSelectedCar.set(lastSelectedCar)
        visibleSelectedCar.set(lastSelectedCar.isNotEmpty())
    }
}