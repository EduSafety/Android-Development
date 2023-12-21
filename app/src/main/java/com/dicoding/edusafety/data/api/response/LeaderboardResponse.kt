package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: List<DataLeaderboard?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataLeaderboard(

	@field:SerializedName("total_complaints")
	val totalComplaints: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)
