package com.margge.services.models

import com.google.gson.annotations.SerializedName

data class BuildDate(@SerializedName("wkda") val buildDate: List<Pair<String, String>>)