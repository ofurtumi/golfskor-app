package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import hugbo.golfskor.entities.Round
import hugbo.golfskor.entities.User
import hugbo.golfskor.ui.states.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
   private val _uiState = MutableStateFlow(ProfileUiState())
   val uiState: StateFlow<ProfileUiState> = _uiState

    val username: String = savedStateHandle.get<String>("username") ?: ""
    val password: String = savedStateHandle.get<String>("password") ?: ""

    init {
        Log.d("myTag", "ProfileViewModel initiated")
        // this user should be gotten from api
        // TODO: Fetch user from api
        val tempUser = User(
            id = 1,
            username,
            authToken = password
        )
        tempUser.addRound(
            Round(
                id = 1,
                courseName = "Test Course",
                username = tempUser.getUsername(),
                holes = listOf(3, 4, 5, 4, 3, 4, 5, 4, 3)
            )
        )
        _uiState.value = ProfileUiState(tempUser)
    }

    

    fun logUserinfo() {
        Log.d("myTag", "User info: ${_uiState.value.user}")
    }
}