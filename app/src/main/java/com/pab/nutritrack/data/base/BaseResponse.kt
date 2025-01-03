package com.pab.nutritrack.data.base

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("msg")
    val msg: String
)