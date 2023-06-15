package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class ServiceGetResponse(

	@field:SerializedName("data")
	val data: List<ServiceGetItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ServiceGetItem(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("jenis_service")
	val jenisService: String
)
