package labs.nusantara.smartrinsebusiness.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.OwnerItem
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SettingViewModel (private val repository: LaundryRepository) : ViewModel() {
    val listOwnerDetail: LiveData<List<OwnerItem>> = repository.listOwnerDetail
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun getOwnerDetail(token: String, ownerId: String) {
        viewModelScope.launch {
            repository.getOwnerDetail(token, ownerId)
        }
    }

    fun putProfileOwner(token: String, ownerId: String, userTelp: RequestBody, userCity: RequestBody, userGender: RequestBody, imageMultipart: MultipartBody.Part) {
        viewModelScope.launch {
            repository.putProfileOwner(token, ownerId, userTelp, userCity, userGender, imageMultipart)
        }
    }
}