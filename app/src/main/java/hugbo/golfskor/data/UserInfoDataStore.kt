package hugbo.golfskor.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

class UserInfoDataStore(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    suspend fun saveUserInfo(username: String, authToken: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[AUTH_TOKEN_KEY] = authToken
        }
    }

    suspend fun clearUserInfo() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    suspend fun getUserInfo(): Pair<String, String> {
        val preferences = dataStore.data.first()
        val username = preferences[USERNAME_KEY] ?: ""
        val authToken = preferences[AUTH_TOKEN_KEY] ?: ""
        return Pair(username, authToken)
    }
}