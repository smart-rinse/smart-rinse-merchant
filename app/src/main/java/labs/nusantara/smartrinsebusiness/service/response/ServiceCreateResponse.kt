package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class ServiceCreateResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("service")
	val service: Service,

	@field:SerializedName("message")
	val message: String
)

data class Service(

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("ownerId")
	val ownerId: String,

	@field:SerializedName("laundryId")
	val laundryId: String,

	@field:SerializedName("jenis_service")
	val jenisService: String
)
