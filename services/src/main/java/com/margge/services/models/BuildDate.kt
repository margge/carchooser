package com.margge.services.models

import com.google.gson.annotations.SerializedName

data class BuildDate(@SerializedName("wkda") val buildDateMap: HashMap<String, String>)