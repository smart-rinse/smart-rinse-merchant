package labs.nusantara.smartrinsebusiness.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.response.OrdersItemItem
import labs.nusantara.smartrinsebusiness.utils.Event
import labs.nusantara.smartrinsebusiness.utils.SessionModel

class OrderViewModel (private val repository: LaundryRepository) : ViewModel() {
    val listDataTrx: LiveData<List<List<OrdersItemItem>>> = repository.listTrxOwner
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession()
    }

    fun getOrderTransaction(token: String) {
        viewModelScope.launch {
            repository.getOrderTrx(token)
        }
    }

    fun putOrderTransaction(token: String, idTrx: String) {
        viewModelScope.launch {
            repository.putOrderTrx(token, idTrx)
        }
    }
}