package labs.nusantara.smartrinsebusiness.ui.layanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.MerchantDataItem
import labs.nusantara.smartrinsebusiness.service.response.ServiceCreateResponse
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel

class ServiceViewModel (private val repository: LaundryRepository) : ViewModel() {
    val serviceCreateResponse: LiveData<ServiceCreateResponse> = repository.serviceCreateResponse
    val listMerchantOwner: LiveData<List<MerchantDataItem>> = repository.listMerchant
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun postService(token: String, laundryId: String, jenis: String, price: String) {
        viewModelScope.launch {
            repository.postService(token, laundryId, jenis, price)
        }
    }

    fun getMerchantInfo(token: String) {
        viewModelScope.launch {
            repository.getMerchantOwner(token)
        }
    }
}