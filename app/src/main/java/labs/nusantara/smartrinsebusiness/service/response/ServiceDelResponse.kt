package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class ServiceDelResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)
