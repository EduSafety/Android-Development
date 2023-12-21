package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryComplaintResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: DataComplaint? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataComplaint(

	@field:SerializedName("violence_label")
	val violenceLabel: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("category_name")
	val categoryName: String? = null,

	@field:SerializedName("incident_date")
	val incidentDate: Any? = null,

	@field:SerializedName("perpetrator")
	val perpetrator: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("victim")
	val victim: String? = null,

	@field:SerializedName("institution_name")
	val institutionName: String? = null
)
