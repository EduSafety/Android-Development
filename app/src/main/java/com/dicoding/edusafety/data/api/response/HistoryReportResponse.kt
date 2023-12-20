package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryReportResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: DataReport? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataReport(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("pages")
	val pages: Int? = null,

	@field:SerializedName("record")
	val record: List<RecordReportItem?>? = null,

	@field:SerializedName("limit")
	val limit: String? = null,

	@field:SerializedName("page")
	val page: String? = null
)

data class RecordReportItem(

	@field:SerializedName("complaint_id")
	val complaintId: Int? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("incident_date")
	val incidentDate: String? = null,

	@field:SerializedName("perpetrator")
	val perpetrator: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("victim")
	val victim: String? = null
)
