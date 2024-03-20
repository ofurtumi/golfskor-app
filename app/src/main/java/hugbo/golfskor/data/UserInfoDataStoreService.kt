package hugbo.golfskor.data

import android.content.Context

object UserInfoDataStoreService {
    private lateinit var userInfoDataStore: UserInfoDataStore

    fun init(context: Context) {
        userInfoDataStore = UserInfoDataStore(context)
    }

    suspend fun saveUserInfo(username: String, authToken: String) {
        userInfoDataStore.saveUserInfo(username, authToken)
    }

    suspend fun clearUserInfo() {
        userInfoDataStore.clearUserInfo()
    }

    suspend fun getUserInfo(): Pair<String, String> {
        return userInfoDataStore.getUserInfo()
    }
}