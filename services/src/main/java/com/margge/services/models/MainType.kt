package com.margge.services.models

import com.google.gson.annotations.SerializedName

data class MainType(val page: Int,
                    val pageSize: Int,
                    val totalPageCount: Int,
                    @SerializedName("wkda") val mainTypesMap: HashMap<String, String>)