import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPrefrences(
    context: Context
) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
            name = "app_pref"
    )

    companion object{
        val LANGUAGE_CODE_KEY = preferencesKey<String>(name = "language_code")
    }

    suspend fun saveAppLanguage(langCode: String){
        dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE_KEY] = langCode
        }
    }

     val lastAppLanguage: Flow<String> = dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_CODE_KEY] ?: "en"
            }
}


