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

    /**
     * Asynchronously saves user information to a secured preferences DataStore.
     *
     * This function is designed to persistently store user credentials such as username and authentication token,
     * allowing for secure and consistent access across app sessions. It utilizes Kotlin's coroutine support with a
     * suspend modifier, ensuring that the operation is performed without blocking the main thread, thus keeping the
     * UI responsive.
     *
     * The function performs the following operations:
     * - Accesses the DataStore's edit method to begin a transaction that updates the values stored in the preferences.
     * - Updates the preferences with the new username and authentication token provided by the parameters.
     * - The DataStore transaction ensures atomicity and consistency, so the new values completely replace any existing
     *   values associated with the keys.
     *
     * @param username The username to be stored in the DataStore.
     * @param authToken The authentication token to be stored in the DataStore.
     */
    suspend fun saveUserInfo(username: String, authToken: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[AUTH_TOKEN_KEY] = authToken
        }
    }

    /**
     * Asynchronously clears user information from the preferences DataStore.
     *
     * This function is responsible for securely removing stored user credentials, including the username and
     * authentication token, from the application's DataStore. It operates as a suspend function, ensuring that
     * the operation does not block the main thread and maintains UI responsiveness.
     *
     * The function accesses the DataStore's edit method to initiate a transaction that modifies the stored preferences:
     * - Removes the stored username and authentication token entries. This is done by calling the `remove` method
     *   on the preferences object for each specific key.
     * - The changes are atomic, ensuring that once the operation completes, the specified keys are entirely removed,
     *   effectively logging out the user and preventing unauthorized access from any subsequent session without
     *   re-authentication.
     */
    suspend fun clearUserInfo() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    /**
     * Asynchronously retrieves stored user information from the preferences DataStore.
     *
     * This function fetches the current user's username and authentication token from a secured preferences DataStore.
     * It is marked as a suspend function, which allows it to perform this potentially blocking I/O operation off the main
     * thread, thereby keeping the UI responsive. The function ensures that user credentials can be securely accessed
     * and utilized throughout the application without needing to re-authenticate the user frequently.
     *
     * Operations performed by the function include:
     * - Fetching the latest snapshot of the DataStore's data stream, which contains all stored preferences.
     * - Extracting the username and authentication token from the preferences using predefined keys. If the keys do
     *   not exist or the values are null, default empty strings are returned to prevent null pointer exceptions.
     * - Returning the username and authToken as a Pair, which is a convenient container for passing around two related
     *   values.
     *
     * @return A Pair containing the username and authToken as strings. If not present, empty strings are returned.
     */
    suspend fun getUserInfo(): Pair<String, String> {
        val preferences = dataStore.data.first()
        val username = preferences[USERNAME_KEY] ?: ""
        val authToken = preferences[AUTH_TOKEN_KEY] ?: ""
        return Pair(username, authToken)
    }
}