package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class NavUiState(
    val username: String,
    val userId: Int,
    val authToken: String
)

class NavViewModel : ViewModel() {
    var navUiState: NavUiState by mutableStateOf(NavUiState("", 0, ""))
        private set

    fun setUserInfo(username: String, userId: Int, authToken: String) {
        navUiState = NavUiState(username, userId, authToken)
    }

    fun clearUserInfo() {
        navUiState = NavUiState("", 0, "")
    }
}
