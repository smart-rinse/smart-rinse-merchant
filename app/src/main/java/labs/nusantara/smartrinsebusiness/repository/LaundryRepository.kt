package labs.nusantara.smartrinsebusiness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import labs.nusantara.smartrinsebusiness.service.api.APIService
import labs.nusantara.smartrinsebusiness.service.response.*
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel
import labs.nusantara.smartrinsebusiness.utils.SessionPreferences
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
        namaMerchant: String,
        tglBerdiri: String,
        alamat: String,
        latitude: String,
        longitude: String,
        jamBuka: String,
        jamTutup: String,
        rekening: Int
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
            rekening
        )

        client.enqueue(object : Callback<MerchantCreateResponse> {
            override fun onResponse(
                call: Call<MerchantCreateResponse>,
                response: Response<MerchantCreateResponse>
            ) {
                _isLoading.value = false
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