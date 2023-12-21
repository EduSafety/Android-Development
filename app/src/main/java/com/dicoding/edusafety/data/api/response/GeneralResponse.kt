package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: GeneralData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GeneralData(

	@field:SerializedName("id")
	val id: Int? = null
)
