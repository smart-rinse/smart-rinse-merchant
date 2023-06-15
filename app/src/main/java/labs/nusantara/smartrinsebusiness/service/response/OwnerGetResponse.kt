package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class OwnerGetResponse(

	@field:SerializedName("owner")
	val owner: OwnerItem,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class OwnerItem(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("telephone")
	val telephone: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("isLaundry")
	val isLaundry: Boolean
)
