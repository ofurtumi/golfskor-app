package hugbo.golfskor.ui.states

data class AuthenticateUiState(
    val username: String = "",
    val password: String = "",
    val newUser: Boolean = false,
    val loggedIn: Boolean = false,
    val message: String = "",
    val isAuthErr: Boolean = false
)
