package labs.nusantara.smartrinsebusiness.service.api

import labs.nusantara.smartrinsebusiness.service.response.LoginResponse
import labs.nusantara.smartrinsebusiness.service.response.MerchantCreateResponse
import labs.nusantara.smartrinsebusiness.service.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("laundry/create")
    fun postMerchant(
        @Header("Authorization") token: String,
        @Field("nama_laundry") nama_laundry: String,
        @Field("tanggal_berdiri") tanggal_berdiri: String,
        @Field("alamat") alamat: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("jam_buka") jam_buka: String,
        @Field("jam_tutup") jam_tutup: String,
        @Field("photo") photo: String,
        @Field("rekening") rekening: Int
    ): Call<MerchantCreateResponse>
}