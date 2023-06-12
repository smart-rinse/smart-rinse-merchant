package labs.nusantara.smartrinsebusiness.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import labs.nusantara.smartrinsebusiness.repository.LaundryRepository
import labs.nusantara.smartrinsebusiness.service.api.APIConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    "token"
)

object Injection {
    fun provideRepository(context: Context): LaundryRepository {
        val apiService = APIConfig().getApiService()
        val preferences = SessionPreferences.getInstance(context.dataStore)
        return LaundryRepository.getInstance(preferences, apiService)
    }
}