package labs.nusantara.smartrinsebusiness.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.utils.SessionModel

class SettingViewModel (private val repository: LaundryRepository) : ViewModel() {

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }
}