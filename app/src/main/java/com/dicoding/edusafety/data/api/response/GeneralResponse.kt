package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("message")
	val message: String? = null
)
