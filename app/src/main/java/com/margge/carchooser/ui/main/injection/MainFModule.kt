package com.margge.carchooser.ui.main.injection

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.margge.carchooser.helpers.SharedPrefHelper
import com.margge.carchooser.ui.main.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainFModule(private val fragmentActivity: FragmentActivity) {

    @Singleton
    @Provides
    fun providesMainViewModel(): MainViewModel =
            ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)

    @Provides
    @Singleton
    fun provideSharedPrefHelper(): SharedPrefHelper =
            SharedPrefHelper(fragmentActivity.application.getSharedPreferences(
                    SharedPrefHelper.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE))
}