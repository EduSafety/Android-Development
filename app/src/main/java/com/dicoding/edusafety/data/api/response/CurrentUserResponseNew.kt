package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class CurrentUserResponseNew(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: DataNew? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataNew(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("access_code")
	val accessCode: String? = null,

	@field:SerializedName("verified")
	val verified: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("institution_id")
	val institutionId: Int? = null
)
