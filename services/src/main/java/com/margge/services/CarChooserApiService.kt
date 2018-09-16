package com.margge.services

import com.margge.services.models.BuildDate
import com.margge.services.models.MainType
import com.margge.services.models.Manufacturer
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CarChooserApiService {

    @GET("v1/car-types/manufacturer")
    fun getManufacturers(@Query("wa_key") key: String = WA_KEY,
                         @Query("page") page: Int = 0,
                         @Query("pageSize") pageSize: Int = 0): Observable<Manufacturer>

    @GET("v1/car-types/main-types")
    fun getMainTypes(@Query("wa_key") key: String = WA_KEY,
                     @Query("page") page: Int = 0,
                     @Query("pageSize") pageSize: Int = 0,
                     @Query("manufacturer") manufacturer: String): Observable<MainType>

    @GET("v1/car-types/built-dates")
    fun getBuildDates(@Query("wa_key") key: String = WA_KEY,
                      @Query("page") page: Int = 0,
                      @Query("pageSize") pageSize: Int = 0,
                      @Query("manufacturer") manufacturer: String,
                      @Query("main-type") mainType: String): Observable<BuildDate>

    companion object {

        private val WA_KEY: String = "coding-puzzle-client-449cc9d"

        fun create(): CarChooserApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://api-aws-eu-qa-1.auto1-test.com/")
                    .build()

            return retrofit.create(CarChooserApiService::class.java)
        }
    }
}