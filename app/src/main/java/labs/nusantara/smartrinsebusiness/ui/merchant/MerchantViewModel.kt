package labs.nusantara.smartrinsebusiness.ui.merchant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.MerchantCreateResponse
import labs.nusantara.smartrinsebusiness.service.response.MerchantDataItem
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MerchantViewModel(private val repository: LaundryRepository) : ViewModel() {
    val laundryCreateResponse: LiveData<MerchantCreateResponse> = repository.merchantCreateResponse
    val listMerchantOwner: LiveData<List<MerchantDataItem>> = repository.listMerchant
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

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
        viewModelScope.launch {
            repository.postMerchant(
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
        }
    }

    fun getMerchantInfo(token: String) {
        viewModelScope.launch {
            repository.getMerchantOwner(token)
        }
    }
}