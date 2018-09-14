package com.margge.carchooser.ui.main.injection

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.margge.carchooser.ui.main.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainFModule (private val fragmentActivity: FragmentActivity) {

    @Singleton
    @Provides
    fun providesMainViewModel(): MainViewModel =
            ViewModelProviders.of(fragmentActivity).get(MainViewModel::class.java)
}