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

    @Inject lateinit var mSharedPrefHelper: SharedPrefHelper

    val mSearchViewEvents: MutableLiveData<Event> = MutableLiveData()
    private val mCarChooserRepository: CarChooserApiService by lazy { (application as CarChooserApplication).carChooserApiService }

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
                getModels(mSharedPrefHelper[LAST_CAR_MANUFACTURER_ID, ""])
            CAR_YEAR ->
                getYears(mSharedPrefHelper[LAST_CAR_MANUFACTURER_ID, ""], mSharedPrefHelper[LAST_CAR_MODEL_ID, ""])
        }
    }

    private fun getManufacturers() {
        mCarChooserRepository.getManufacturers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    mSearchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.manufacturersMap.toList().sortedBy { it.second }))
                }, {
                    mSearchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    private fun getModels(manufacturer: String) {
        mCarChooserRepository.getMainTypes(manufacturer = manufacturer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    mSearchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.mainTypesMap.toList().sortedBy { it.second }))
                }, {
                    mSearchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    private fun getYears(manufacturer: String, model: String) {
        mCarChooserRepository.getBuildDates(manufacturer = manufacturer, mainType = model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    mSearchViewEvents.postValue(Event(Event.EventsType.DataLoaded,
                            result.buildDateMap.toList().sortedBy { it.second }))
                }, {
                    mSearchViewEvents.postValue(Event(Event.EventsType.ConnectionError))
                })
    }

    fun saveSelectedData(dataType: String, selectedData: Pair<String, String>) {
        when (dataType) {
            CAR_MANUFACTURER -> {
                mSharedPrefHelper.put(LAST_CAR_MANUFACTURER_ID, selectedData.first)
                mSharedPrefHelper.put(LAST_CAR_MANUFACTURER, selectedData.second)
            }
            CAR_MODEL -> {
                mSharedPrefHelper.put(LAST_CAR_MODEL_ID, selectedData.first)
                mSharedPrefHelper.put(LAST_CAR_MODEL, selectedData.second)
            }
            CAR_YEAR -> {
                mSharedPrefHelper.put(LAST_CAR_YEAR_ID, selectedData.first)
                mSharedPrefHelper.put(LAST_CAR_YEAR, selectedData.second)

                mSharedPrefHelper.put(SharedPrefHelper.LAST_SEARCHES, mSharedPrefHelper[LAST_SELECTED_CAR, ""] +
                        "${mSharedPrefHelper[SharedPrefHelper.LAST_SEARCHES, ""]} \n")

                mSharedPrefHelper.put(LAST_SELECTED_CAR, "${mSharedPrefHelper[LAST_CAR_MANUFACTURER, ""]}, " +
                        "${mSharedPrefHelper[LAST_CAR_MODEL, ""]}, " +
                        "${mSharedPrefHelper[LAST_CAR_YEAR, ""]} \n")
            }
        }
    }
}