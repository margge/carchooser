package com.margge.services.models

import com.google.gson.annotations.SerializedName

data class Manufacturer(val page: Int,
                        val pageSize: Int,
                        val totalPageCount: Int,
                        @SerializedName("wkda") val manufacturersMap: HashMap<String, String>)