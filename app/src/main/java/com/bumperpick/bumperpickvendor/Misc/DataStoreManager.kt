// DataStoreExtensions.kt (create a separate file or place at top of DataStoreManager.kt)
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.core.DataStore



import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bumperpick.bumperpickvendor.API.FinalModel.Data
import com.bumperpick.bumperpickvendor.API.FinalModel.Meta
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONObject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")
class DataStoreManager(private val context: Context) {

    companion object {

        private val VENDOR_DETAILS= stringPreferencesKey("vendor_details")
        private val VendorToken= stringPreferencesKey("vendor_token")
    }

    private val dataStore = context.dataStore


    val json= Gson()
    suspend fun save_Vendor_Details(details: Data){

        dataStore.edit { prefs->
            prefs[VENDOR_DETAILS]=json.toJson(details)
        }


    }
    suspend fun get_Vendor_Details():Data?
    { val d= dataStore.data.map { prefs-> prefs[VENDOR_DETAILS] } .firstOrNull()
        return json.fromJson(d,Data::class.java)
    }

    suspend fun saveToken(Meta:Meta){
        dataStore.edit { prefs->
            prefs[VendorToken]=json.toJson(Meta)
        }
    }
    suspend fun getToken():Meta?{
        val d= dataStore.data.map { prefs-> prefs[VendorToken] } .firstOrNull()
        return json.fromJson(d,Meta::class.java)
    }

    suspend fun clearToken(){
        dataStore.edit { prefs->
            prefs.remove(VendorToken)
            prefs.remove(VENDOR_DETAILS)
        }
    }


}
