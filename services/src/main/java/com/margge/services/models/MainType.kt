package com.margge.services.models

import com.google.gson.annotations.SerializedName

data class MainType(val page: Int,
                    val pageSize: Int,
                    val totalPageCount: Int,
                    @SerializedName("wkda") val mainTypesList: List<Pair<String, String>>)