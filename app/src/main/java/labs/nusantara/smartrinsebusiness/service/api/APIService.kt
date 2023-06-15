package labs.nusantara.smartrinsebusiness.service.api

import labs.nusantara.smartrinsebusiness.service.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("owner/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("owner/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("laundry/create")
    fun postMerchant(
        @Header("Authorization") token: String,
        @Part("nama_laundry") nama_laundry: RequestBody,
        @Part("tanggal_berdiri") tanggal_berdiri: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("jam_buka") jam_buka: RequestBody,
        @Part("jam_tutup") jam_tutup: RequestBody,
        @Part("rekening") rekening: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("telephone") telephone: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<MerchantCreateResponse>

    @Multipart
    @PUT("laundry/{laundryId}")
    fun putMerchantPhoto(
        @Header("Authorization") token: String,
        @Path("laundryId") laundryId: String,
        @Part("nama_laundry") nama_laundry: RequestBody,
        @Part("tanggal_berdiri") tanggal_berdiri: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("jam_buka") jam_buka: RequestBody,
        @Part("jam_tutup") jam_tutup: RequestBody,
        @Part("rekening") rekening: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("telephone") telephone: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<MerchantPutResponse>

    @FormUrlEncoded
    @POST("service/{laundryId}")
    fun postService(
        @Header("Authorization") token: String,
        @Path("laundryId") laundryId: String,
        @Field("jenis_service") jenis_service: String,
        @Field("price") price: String
    ): Call<ServiceCreateResponse>

    @GET("owner/get/laundry")
    fun getMerchantOwner(
        @Header("Authorization") token: String
    ): Call<MerchantOwnerGetResponse>

    @GET("service/{laundryId}")
    fun getServiceOwner(
        @Header("Authorization") token: String,
        @Path("laundryId") laundryId: String
    ): Call<ServiceGetResponse>

    @DELETE("service/{serviceId}")
    fun delServiceOwner(
        @Header("Authorization") token: String,
        @Path("serviceId") serviceId: Int
    ): Call<ServiceDelResponse>

    @GET("owner/transaction")
    fun getOwnerTrx(
        @Header("Authorization") token: String
    ): Call<OrderGetResponse>

    @PUT("owner/status/{trxId}")
    fun putOwnerTrx(
        @Header("Authorization") token: String,
        @Path("trxId") trxId: String
    ): Call<TrxUpdateResponse>

    @GET("owner/{ownerId}")
    fun getOwnerDetail(
        @Header("Authorization") token: String,
        @Path("ownerId") ownerId: String
    ): Call<OwnerGetResponse>

    @Multipart
    @PUT("owner/editOwner/{ownerId}")
    fun putProfileOwner(
        @Header("Authorization") token: String,
        @Path("ownerId") ownerId: String,
        @Part("telephone") telephone: RequestBody,
        @Part("city") city: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part photo: MultipartBody.Part,
    ): Call<OwnerUpdateResponse>
}