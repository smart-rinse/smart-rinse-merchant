package labs.nusantara.smartrinsebusiness.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.RegisterResponse
import labs.nusantara.smartrinsebusiness.utils.Event

class RegisterViewModel(private val repository: LaundryRepository) : ViewModel() {
    val registerResponse: LiveData<RegisterResponse> = repository.regResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun postRegister(name: String, email: String, password: String, confPassword: String) {
        viewModelScope.launch {
            repository.postRegister(name, email, password, confPassword)
        }
    }
}