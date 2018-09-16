package com.margge.carchooser.ui.main.injection

import android.content.Context
import com.margge.carchooser.base.CarChooserApplication
import com.margge.carchooser.helpers.SharedPrefHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainVMModule(private val application: CarChooserApplication) {

    @Provides
    @Singleton
    fun provideSharedPrefHelper(): SharedPrefHelper =
            SharedPrefHelper(application.getSharedPreferences(
                    SharedPrefHelper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE))
}