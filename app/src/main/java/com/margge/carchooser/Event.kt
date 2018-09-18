package com.margge.carchooser

data class Event(val name: EventsType, val data: Any? = null) {

    sealed class EventsType {
        object GetData : EventsType()
        object MissingValue : EventsType()
        object DataLoaded : EventsType()
        object SelectedItem : EventsType()
        object ConnectionError : EventsType()
    }
}