// DataStoreExtensions.kt (create a separate file or place at top of DataStoreManager.kt)
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.core.DataStore



import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")
class DataStoreManager(private val context: Context) {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
    }

    private val dataStore = context.dataStore

    suspend fun saveUserId(userId: String) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    val getUserId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_ID]
    }
}
