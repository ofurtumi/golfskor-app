package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.data.UserInfoDataStoreService
import hugbo.golfskor.entities.ApiAuth
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    data object Starting : AuthUiState
    data object LoginForm : AuthUiState
    data object RefreshLoading : AuthUiState
    data object LoginLoading : AuthUiState
    data object SignupLoading : AuthUiState
    data class Success(val auth: ApiAuth) : AuthUiState
    data object Registered : AuthUiState
    data class Error(val login: Boolean) : AuthUiState
    data object ExpiredToken : AuthUiState
}

class AuthenticateViewModel : ViewModel() {
    var authUiState: AuthUiState by mutableStateOf(AuthUiState.Starting)
        private set

    /**
     * Initialization block for the ViewModel handling authentication.
     *
     * This block is executed when an instance of the ViewModel is created. It initiates an asynchronous operation
     * to fetch stored user credentials (username and authentication token) and determines the initial state of the
     * authentication UI based on these credentials.
     *
     * The process includes:
     * - Retrieving user information from a local data store.
     * - Checking if the stored username and token are not empty. If valid credentials exist, it proceeds to refresh
     *   the token by making an API call.
     * - On successful token refresh, the authentication state is set to {@code AuthUiState.Success}, which includes
     *   the new authentication details received from the server.
     * - If the token refresh fails due to any exception (e.g., network errors, invalid token), the state is set to
     *   {@code AuthUiState.ExpiredToken}, indicating that the existing token is no longer valid.
     * - If no valid credentials are stored (i.e., username or token is empty), the state is set to {@code AuthUiState.LoginForm},
     *   prompting the user to log in.
     *
     * The use of {@code viewModelScope} ensures that the coroutine launched for this process is automatically canceled
     * if the ViewModel is cleared, preventing memory leaks and ensuring that the ViewModel's lifecycle is properly managed.
     */
    init {
        viewModelScope.launch {
            val (username, authToken) = UserInfoDataStoreService.getUserInfo()
            if (username.isNotEmpty() && authToken.isNotEmpty()) {
                authUiState = AuthUiState.RefreshLoading
                authUiState = try {
                    val authResult = GolfSkorApi.retrofitService.refreshToken(
                        username,
                        "Bearer $authToken"
                    )
                    AuthUiState.Success(authResult)
                } catch (e: Exception) {
                    AuthUiState.ExpiredToken
                }
            } else {
                authUiState = AuthUiState.LoginForm
            }
        }
    }

    /**
     * Attempts to authenticate a user with the provided username and password.
     *
     * This function is called to verify user credentials against a server using an API call. It sets the authentication
     * state to {@code AuthUiState.LoginLoading} to indicate that a login attempt is in progress. A coroutine is launched
     * within the {@code viewModelScope} to perform the asynchronous login request.
     *
     * The function executes the following steps:
     * - Makes an API call to login with the specified username and password.
     * - On successful authentication, saves the received authentication token using {@code UserInfoDataStoreService} and updates
     *   the authentication state to {@code AuthUiState.Success}, which includes the authentication result.
     * - If the login attempt fails due to any exception (e.g., network issues, incorrect credentials), updates the authentication
     *   state to {@code AuthUiState.Error}, specifically indicating it as a login error.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     */
    fun checkCredentials(username: String, password: String) {
        authUiState = AuthUiState.LoginLoading
        viewModelScope.launch {
            authUiState = try {
                val authResult = GolfSkorApi.retrofitService.login(username, password)
                UserInfoDataStoreService.saveUserInfo(username, authResult.authToken)
                AuthUiState.Success(authResult)
            } catch (e: Exception) {
                AuthUiState.Error(true)
            }
        }
    }

    /**
     * Registers a new user with the specified username and password.
     *
     * This function initiates the user registration process by first setting the authentication state to
     * {@code AuthUiState.SignupLoading} to indicate that a registration attempt is underway. It then launches
     * a coroutine within the {@code viewModelScope} to perform the asynchronous registration request.
     *
     * The function performs the following operations:
     * - Makes an API call to register the user with the provided username and password.
     * - On successful registration, updates the authentication state to {@code AuthUiState.Registered}, signaling
     *   that the user has been successfully registered.
     * - If the registration attempt fails due to any exception (e.g., network issues, username already taken),
     *   updates the authentication state to {@code AuthUiState.Error}, specifically indicating it as a registration error.
     *
     * @param username The username to be registered. Must be unique across the platform.
     * @param password The password for the new user account.
     */
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