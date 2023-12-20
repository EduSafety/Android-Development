package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class CurrentUserResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("access_code")
	val accessCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("institution_id")
	val institutionId: Int? = null
)
