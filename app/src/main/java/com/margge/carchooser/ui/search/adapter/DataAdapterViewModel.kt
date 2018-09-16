package com.margge.carchooser.ui.search.adapter

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.margge.carchooser.Event

class DataAdapterViewModel(application: Application, private val dataItemEvents: MutableLiveData<Event>)
    : AndroidViewModel(application) {

    val itemText = ObservableField("")
    lateinit var data: Pair<String, String>

    fun bindData(itemInfo: Pair<String, String>) {
        data = itemInfo
        itemText.set(itemInfo.second)
    }

    fun showSelectedItem() =
            dataItemEvents.postValue(Event(Event.EventsType.SelectedItem, data))

}