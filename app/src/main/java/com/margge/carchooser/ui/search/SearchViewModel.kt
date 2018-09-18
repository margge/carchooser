package com.margge.carchooser.ui.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.margge.carchooser.Event
import com.margge.carchooser.base.CarChooserApplication
import com.margge.carchooser.helpers.SharedPrefHelper
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MANUFACTURER
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MANUFACTURER_ID
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MODEL
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_MODEL_ID
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_YEAR
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_CAR_YEAR_ID
import com.margge.carchooser.helpers.SharedPrefHelper.Companion.LAST_SELECTED_CAR
import com.margge.carchooser.ui.main.MainViewModel.Companion.CAR_MANUFACTURER
import com.margge.carchooser.ui.main.MainViewModel.Companion.CAR_MODEL
import com.margge.carchooser.ui.main.MainViewModel.Companion.CAR_YEAR
import com.margge.carchooser.ui.search.injection.DaggerSearchVMComponent
import com.margge.carchooser.ui.search.injection.SearchVMModule
import com.margge.services.CarChooserApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var sharedPrefHelper: SharedPrefHelper

    val searchViewEvents: MutableLiveData<Event> = MutableLiveData()
    private val carChooserRepository: CarChooserApiService by lazy { (application as CarChooserApplication).carChooserApiService }

    init {
        DaggerSearchVMComponent.builder()
                .searchVMModule(SearchVMModule(application as CarChooserApplication))
                .build()
                .inject(this@SearchViewModel)
    }

    fun getData(dataType: String) {
        when (dataType) {
            CAR_MANUFACTURER ->
                getManufacturers()
            CAR_MODEL ->
                getModels(sharedPrefHelper[LAST_CAR_MANUFACTURER_ID, ""])
            CAR_YEAR ->
                getYears(sharedPrefHelper[LAST_CAR_MANUFACTURER_ID, ""], sharedPrefHelper[LAST_CAR_MODEL_ID, ""])
        }
    }

    private fun getManufacturers() {
        carChooserRepository.getManufacturers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    searchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.manufacturersMap.toList().sortedBy { it.second }))

                    sharedPrefHelper.put(LAST_CAR_MANUFACTURER, "")
                    sharedPrefHelper.put(LAST_CAR_MODEL, "")
                }, {
                    searchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    private fun getModels(manufacturer: String) {
        carChooserRepository.getMainTypes(manufacturer = manufacturer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    searchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.mainTypesMap.toList().sortedBy { it.second }))
                }, {
                    searchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    private fun getYears(manufacturer: String, model: String) {
        carChooserRepository.getBuildDates(manufacturer = manufacturer, mainType = model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    searchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.buildDateMap.toList().sortedBy { it.second }))
                }, {
                    searchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    fun saveSelectedData(dataType: String, selectedData: Pair<String, String>) {
        when (dataType) {
            CAR_MANUFACTURER -> {
                sharedPrefHelper.put(LAST_CAR_MANUFACTURER_ID, selectedData.first)
                sharedPrefHelper.put(LAST_CAR_MANUFACTURER, selectedData.second)
            }
            CAR_MODEL -> {
                sharedPrefHelper.put(LAST_CAR_MODEL_ID, selectedData.first)
                sharedPrefHelper.put(LAST_CAR_MODEL, selectedData.second)
            }
            CAR_YEAR -> {
                sharedPrefHelper.put(LAST_CAR_YEAR_ID, selectedData.first)
                sharedPrefHelper.put(LAST_CAR_YEAR, selectedData.second)

                sharedPrefHelper.put(SharedPrefHelper.LAST_SEARCHES, sharedPrefHelper[LAST_SELECTED_CAR, ""] +
                        "${sharedPrefHelper[SharedPrefHelper.LAST_SEARCHES, ""]} \n")

                sharedPrefHelper.put(LAST_SELECTED_CAR, "${sharedPrefHelper[LAST_CAR_MANUFACTURER, ""]}, " +
                        "${sharedPrefHelper[LAST_CAR_MODEL, ""]}, " +
                        "${sharedPrefHelper[LAST_CAR_YEAR, ""]} \n")
            }
        }
    }
}