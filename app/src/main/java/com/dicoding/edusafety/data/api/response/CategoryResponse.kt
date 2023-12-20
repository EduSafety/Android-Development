package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: DataCategory? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RecordItem(

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class DataCategory(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("pages")
	val pages: Int? = null,

	@field:SerializedName("record")
	val record: List<RecordItem?>? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)
