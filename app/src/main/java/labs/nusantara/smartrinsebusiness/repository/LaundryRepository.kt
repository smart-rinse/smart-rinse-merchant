package labs.nusantara.smartrinsebusiness.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import labs.nusantara.smartrinsebusiness.service.api.APIService
import labs.nusantara.smartrinsebusiness.service.response.*
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel
import labs.nusantara.smartrinsebusiness.utils.SessionPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaundryRepository private constructor(
    private val preferences: SessionPreferences,
    private val apiService: APIService
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    //--------------------------------------------------------


    private val _regResponse = MutableLiveData<RegisterResponse>()
    val regResponse: LiveData<RegisterResponse> = _regResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _merchantCreateResponse = MutableLiveData<MerchantCreateResponse>()
    val merchantCreateResponse: LiveData<MerchantCreateResponse> = _merchantCreateResponse

    private val _serviceCreateResponse = MutableLiveData<ServiceCreateResponse>()
    val serviceCreateResponse: LiveData<ServiceCreateResponse> = _serviceCreateResponse

    private val _listMerchant = MutableLiveData<List<MerchantDataItem>>()
    val listMerchant: LiveData<List<MerchantDataItem>> = _listMerchant

    //--------------------------------------------------------
    //POST REGISTER
    //--------------------------------------------------------
    fun postRegister(name: String, email: String, password: String, confPassword: String) {
        _isLoading.value = true
        val client = apiService.postRegister(name, email, password, confPassword)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _regResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event("Registrasi gagal, periksa isian form Anda.")
                    Log.e(
                        TAG,
                        "ErrorMessage: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "ErrorMessage: ${t.message.toString()}")
            }
        })
    }

    //--------------------------------------------------------
    //POST LOGIN
    //--------------------------------------------------------
    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postLogin(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event("Login Failed")
                    Log.e(
                        TAG,
                        "ErrorMessage: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "ErrorMessage: ${t.message.toString()}")
            }
        })
    }

    //--------------------------------------------------------
    //POST MERCHANT
    //--------------------------------------------------------
    fun postMerchant(
        token: String,
        namaMerchant: RequestBody,
        tglBerdiri: RequestBody,
        alamat: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        jamBuka: RequestBody,
        jamTutup: RequestBody,
        rekening: RequestBody,
        bank: RequestBody,
        telp: RequestBody,
        imageMultipart: MultipartBody.Part
    ) {
        _isLoading.value = true
        val client = apiService.postMerchant(
            token,
            namaMerchant,
            tglBerdiri,
            alamat,
            latitude,
            longitude,
            jamBuka,
            jamTutup,
            rekening,
            bank,
            telp,
            imageMultipart
        )

        Log.d("DATA CLIENT : ", client.toString())
        Log.d("DATA : ", "$token, $namaMerchant, $tglBerdiri, $alamat, $latitude, $longitude, $jamBuka, $jamTutup, $rekening, $imageMultipart")

        client.enqueue(object : Callback<MerchantCreateResponse> {
            override fun onResponse(
                call: Call<MerchantCreateResponse>,
                response: Response<MerchantCreateResponse>
            ) {
                _isLoading.value = false
                Log.d("EE : ", response.toString())
                Log.d("EE : ", response.body().toString())
                if (response.isSuccessful && response.body() != null) {
                    _merchantCreateResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event("Pembuatan mechant laundry gagal.")
                    Log.e(
                        TAG,
                        "ErrorMessage: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<MerchantCreateResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "ErrorMessage: ${t.message.toString()}")
            }
        })
    }


    //--------------------------------------------------------
    //POST SERVICE
    //--------------------------------------------------------
    fun postService(token: String, laundryId: String, jenis: String, price: String) {
        _isLoading.value = true
        val client = apiService.postService(token, laundryId, jenis, price)

        client.enqueue(object : Callback<ServiceCreateResponse> {
            override fun onResponse(
                call: Call<ServiceCreateResponse>,
                response: Response<ServiceCreateResponse>
            ) {
                _isLoading.value = false
                Log.d("Err : ", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    _serviceCreateResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event("Penambahan service gagal.")
                    Log.e(
                        TAG,
                        "ErrorMessage: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ServiceCreateResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "ErrorMessage: ${t.message.toString()}")
            }
        })
    }

    //--------------------------------------------------------
    //GET MERCHANT
    //--------------------------------------------------------
    fun getMerchantOwner(token: String) {
        _isLoading.value = true
        val client = apiService.getMerchantOwner(token)

        client.enqueue(object : Callback<MerchantOwnerGetResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<MerchantOwnerGetResponse>,
                response: Response<MerchantOwnerGetResponse>
            ) {
                _isLoading.value = false
                val listSearch = response.body()?.data
                Log.d("RESSP : ", listSearch.toString())
                if (response.isSuccessful) {
                    val lengthItem = listSearch?.size
                    if (lengthItem != null) {
                        _listMerchant.value = listSearch
                    } else {
                        _toastText.value = Event("Merchant tidak tersedia")
                    }
                } else {
                    _toastText.value = Event(response.message())
                    Log.e(TAG, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MerchantOwnerGetResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("No internet connection")
                Log.e(TAG, "Error: ${t.message.toString()}")
            }
        })
    }


    fun getSession(): LiveData<SessionModel> {
        return preferences.getSession().asLiveData()
    }

    suspend fun saveSession(session: SessionModel) {
        preferences.saveSession(session)
    }

    suspend fun login() {
        preferences.login()
    }

    suspend fun logout() {
        preferences.logout()
        _loginResponse.value = LoginResponse(Data("", "", "", "", false), false, "", 0)
    }

    companion object {
        private const val TAG = "LaundryRepository"

        @Volatile
        private var instance: LaundryRepository? = null
        fun getInstance(
            preferences: SessionPreferences,
            apiService: APIService
        ): LaundryRepository = instance ?: synchronized(this) {
            instance ?: LaundryRepository(preferences, apiService)
        }.also { instance = it }
    }
}