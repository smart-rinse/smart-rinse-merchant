package labs.nusantara.smartrinsebusiness.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.LoginResponse
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel

class LoginViewModel (private val repository: LaundryRepository) : ViewModel() {
    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun postLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.postLogin(email, password)
        }
    }

    fun saveSession(session: SessionModel) {
        viewModelScope.launch {
            repository.saveSession(session)
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }
}