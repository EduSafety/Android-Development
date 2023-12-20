package com.dicoding.edusafety.data.api.response

import com.google.gson.annotations.SerializedName

data class QuestionChoiceResponse(

	@field:SerializedName("acknowledge")
	val acknowledge: Boolean? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ChoicesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null
)

data class DataItem(

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("choices")
	val choices: List<ChoicesItem?>? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null
)
