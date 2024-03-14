package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.entities.ApiAuth
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    data object Starting : AuthUiState
    data object LoginLoading : AuthUiState
    data object SignupLoading : AuthUiState
    data class Success(val auth: ApiAuth) : AuthUiState
    data object Registered : AuthUiState
    data class Error(val login: Boolean) : AuthUiState

}

class AuthenticateViewModel : ViewModel() {
    var authUiState: AuthUiState by mutableStateOf(AuthUiState.Starting)
        private set


    fun checkCredentials(username: String, password: String) {
        authUiState = AuthUiState.LoginLoading
        viewModelScope.launch {
            authUiState = try {
                val authResult = GolfSkorApi.retrofitService.login(username, password)
                AuthUiState.Success(authResult)
            } catch (e: Exception) {
                AuthUiState.Error(true)
            }
        }
    }

    fun registerUser(username: String, password: String) {
        authUiState = AuthUiState.SignupLoading
        viewModelScope.launch {
            authUiState = try {
                GolfSkorApi.retrofitService.register(username, password)
                AuthUiState.Registered
            } catch (e: Exception) {
                AuthUiState.Error(false)
            }
        }
    }
}