package com.margge.carchooser.helpers

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(private val mSharedPreferences: SharedPreferences) {

    companion object {
        const val SHARED_PREFERENCES_NAME = "carchooser-prefs"

        /** Selected values **/
        const val LAST_CAR_MANUFACTURER_ID = "lastCarManufacturerId"
        const val LAST_CAR_MANUFACTURER = "lastCarManufacturer"
        const val LAST_CAR_MODEL_ID = "lastCarModelId"
        const val LAST_CAR_MODEL = "lastCarModel"
        const val LAST_CAR_YEAR_ID = "lastCarYearId"
        const val LAST_CAR_YEAR = "lastCarYear"
        const val LAST_SELECTED_CAR = "lastSelectedCar"
        const val LAST_SEARCHES = "lastSearches"
    }

    fun put(key: String, value: String) = mSharedPreferences.edit().putString(key, value).apply()

    operator fun get(key: String, defaultValue: String): String = mSharedPreferences.getString(key, defaultValue)

    fun get(key: String, defaultValue: Int): Int = mSharedPreferences.getInt(key, defaultValue)

    operator fun get(key: String, defaultValue: Float): Float = mSharedPreferences.getFloat(key, defaultValue)

    fun get(key: String, defaultValue: Boolean): Boolean = mSharedPreferences.getBoolean(key, defaultValue)
}