package labs.nusantara.smartrinsebusiness.service.response

import com.google.gson.annotations.SerializedName

data class MerchantCreateResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("laundry")
	val laundry: Laundry,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class Laundry(

	@field:SerializedName("jam_buka")
	val jamBuka: String,

	@field:SerializedName("jam_tutup")
	val jamTutup: String,

	@field:SerializedName("rekening")
	val rekening: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("tanggal_berdiri")
	val tanggalBerdiri: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("nama_laundry")
	val namaLaundry: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("longitude")
	val longitude: String
)
