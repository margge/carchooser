package com.margge.carchooser.base.injection

import com.margge.services.CarChooserApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CarChooserAppModule {

    @Singleton
    @Provides
    fun providesCarChooserApiService(): CarChooserApiService = CarChooserApiService.create()
}