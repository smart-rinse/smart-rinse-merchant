package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class MerchantOwnerGetResponse(

	@field:SerializedName("data")
	val data: List<MerchantDataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class MerchantDataItem(

	@field:SerializedName("jam_tutup")
	val jamTutup: String,

	@field:SerializedName("count_reviews")
	val countReviews: Any,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("average_rating")
	val averageRating: Any,

	@field:SerializedName("telephone")
	val telephone: String,

	@field:SerializedName("tanggal_berdiri")
	val tanggalBerdiri: String,

	@field:SerializedName("nama_laundry")
	val namaLaundry: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("jam_buka")
	val jamBuka: String,

	@field:SerializedName("bank")
	val bank: String,

	@field:SerializedName("rekening")
	val rekening: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("longitude")
	val longitude: String
)
