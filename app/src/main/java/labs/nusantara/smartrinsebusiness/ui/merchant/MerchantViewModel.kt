package labs.nusantara.smartrinsebusiness.ui.merchant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.MerchantCreateResponse
import labs.nusantara.smartrinsebusiness.utils.Event

class MerchantViewModel(private val repository: LaundryRepository) : ViewModel() {
    val laundryCreateResponse: LiveData<MerchantCreateResponse> = repository.merchantCreateResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun postMerchant(
        token: String,
        namaMerchant: String,
        tglBerdiri: String,
        alamat: String,
        latitude: String,
        longitude: String,
        jamBuka: String,
        jamTutup: String,
        photo: String,
        rekening: Int
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
                photo,
                rekening
            )
        }
    }
}