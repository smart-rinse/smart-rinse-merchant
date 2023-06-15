package labs.nusantara.smartrinsebusiness.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.ui.home.HomeViewModel
import labs.nusantara.smartrinsebusiness.ui.layanan.ServiceViewModel
import labs.nusantara.smartrinsebusiness.ui.login.LoginViewModel
import labs.nusantara.smartrinsebusiness.ui.merchant.MerchantViewModel
import labs.nusantara.smartrinsebusiness.ui.order.OrderViewModel
import labs.nusantara.smartrinsebusiness.ui.register.RegisterViewModel
import labs.nusantara.smartrinsebusiness.ui.setting.SettingViewModel

class ViewModelFactory (private val repository: LaundryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MerchantViewModel::class.java) -> {
                MerchantViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> {
                ServiceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}