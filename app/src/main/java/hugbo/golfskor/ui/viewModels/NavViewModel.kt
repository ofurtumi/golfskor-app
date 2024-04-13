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

    /**
     * Updates the navigation state with the current user's information.
     *
     * This function is used to set the user's credentials and identifiers in the navigation state, encapsulated in a
     * {@code NavUiState} object. This allows other components of the application to access user-specific information
     * needed for personalized interactions and API calls. It directly updates the {@code navUiState} variable of the
     * ViewModel.
     *
     * @param username The username of the user.
     * @param userId The unique identifier for the user, typically used in database transactions.
     * @param authToken The authentication token used for secure API calls, proving the user's identity and session validity.
     */
    fun setUserInfo(username: String, userId: Int, authToken: String) {
        navUiState = NavUiState(username, userId, authToken)
    }

    fun clearUserInfo() {
        navUiState = NavUiState("", 0, "")
    }
}
