package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class TrxUpdateResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("transaction")
	val transaction: TransactionStatus
)

data class TransactionStatus(

	@field:SerializedName("isReviewed")
	val isReviewed: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("transactionDate")
	val transactionDate: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("laundryId")
	val laundryId: String,

	@field:SerializedName("status")
	val status: String
)
