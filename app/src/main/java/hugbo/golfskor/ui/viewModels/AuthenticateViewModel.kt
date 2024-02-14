package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hugbo.golfskor.ui.states.AuthenticateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthenticateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticateUiState())
    val uiState: StateFlow<AuthenticateUiState> = _uiState.asStateFlow()

    private var username by mutableStateOf("")
    private var password by mutableStateOf("")

    init {
        Log.d("myTag", "AuthenticateViewModel initiated")
        // TODO: Check if user is already logged in
        _uiState.value = AuthenticateUiState()
        username = ""
        password = ""
    }

    fun updateUsername(inputUsername: String) {
        // Log.d("myTag", "updateUsername: ${_uiState.value}")
        username = inputUsername
        _uiState.update {
            it.copy(
                username = username,
            )
        }
    }

    fun updatePassword(inputPassword: String) {
        // Log.d("myTag", "updatePassword: ${_uiState.value}")
        password = inputPassword
        _uiState.update {
            it.copy(
                password = password,
            )
        }

    }

    fun checkCredentials(): Boolean {
        // Simulate a login check
        // TODO: Replace with actual login check
        Log.d("myTag", "createUser: ${_uiState.value}")
        if (username == "admin" && password == "admin" ||
            password == "${username}12345" ||
            uiState.value.newUser) {
            _uiState.update{
                it.copy(
                    loggedIn = true,
                    message = "Innskráning tókst.",
                    isAuthErr = false
                )
            }
            return true
        } else {
            _uiState.update {
                it.copy(
                    message = "Notendanafn eða lykilorð rangt. Reyndu aftur.",
                    isAuthErr = true
                )
            }
            return false
        }
    }

    fun createUser() {
        // Simulate a user creation
        // TODO: Replace with actual user creation
        if (username != "" && password != "") {
            _uiState.update {
                it.copy(
                    newUser = true, // Automatically log in the user after creation, hacky
                    message = "Notandi ${username} búinn til. Skráðu þig inn.",
                    isAuthErr = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    message = "Notendanafn og lykilorð verða að vera að minnsta kosti 1 stafur.",
                    isAuthErr = true
                )
            }
        }
        Log.d("myTag", "createUser: ${_uiState.value}")
    }
}