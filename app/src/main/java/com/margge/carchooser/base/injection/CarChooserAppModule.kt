package com.margge.carchooser.base.injection

import android.app.Application
import android.content.Context
import com.margge.carchooser.helpers.SharedPrefHelper
import com.margge.services.CarChooserApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CarChooserAppModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesCarChooserApiService(): CarChooserApiService = CarChooserApiService.create()

    @Provides
    @Singleton
    fun provideSharedPrefHelper(): SharedPrefHelper =
            SharedPrefHelper(application.getSharedPreferences(
                    SharedPrefHelper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE))
}