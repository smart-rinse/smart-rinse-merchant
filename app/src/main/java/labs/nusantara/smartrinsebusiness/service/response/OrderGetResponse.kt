package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class OrderGetResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("orders")
	val orders: List<List<OrdersItemItem>>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class OrdersItemItem(

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("dateTransaction")
	val dateTransaction: String,

	@field:SerializedName("totalCost")
	val totalCost: Int,

	@field:SerializedName("idTransaction")
	val idTransaction: String,

	@field:SerializedName("status")
	val status: String
)
