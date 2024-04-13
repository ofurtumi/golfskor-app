package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hugbo.golfskor.R
import hugbo.golfskor.ui.LoadingScreen
import hugbo.golfskor.ui.viewModels.AuthUiState
import hugbo.golfskor.ui.viewModels.AuthenticateViewModel
import hugbo.golfskor.ui.viewModels.NavViewModel

/**
 * Composable function that provides a user interface for authentication.
 *
 * This screen includes components for displaying authentication status, input fields for username and password,
 * and buttons for submitting these credentials for either login or registration. The function reacts dynamically
 * to the current authentication state provided by {@code authViewModel}, adjusting displayed elements accordingly.
 *
 * Depending on the state of authentication (e.g., initial, loading, success, error), different UI elements are
 * displayed. This function also handles navigation after successful authentication by updating user information
 * in {@code navViewModel} and navigating to the profile screen.
 *
 * @param innerPadding Padding applied to the outermost column for layout purposes.
 * @param navController Controller for managing navigation between composables.
 * @param navViewModel ViewModel associated with navigation, used here to update user information.
 * @param authViewModel ViewModel that holds the authentication state and business logic. Defaults to a new viewModel
 *                      instance scoped to the current composable if not provided.
 */
@Composable
fun AuthenticateScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    navViewModel: NavViewModel,
    authViewModel: AuthenticateViewModel = viewModel(),
) {
    val authUiState = authViewModel.authUiState
    val appName = stringResource(R.string.app_name)
    val defaultPadding = Modifier.padding(16.dp, 8.dp)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = appName, fontSize = 54.sp)

        Spacer(modifier = defaultPadding)

        when (authUiState) {
            is AuthUiState.Starting -> {}

            is AuthUiState.RefreshLoading -> {
                LoadingScreen("Skrái þig aftur inn")
            }

            is AuthUiState.LoginForm -> {
                Text(
                    stringResource(R.string.auth_please_login),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is AuthUiState.ExpiredToken -> {
                Text(
                    stringResource(R.string.auth_expired_token),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is AuthUiState.LoginLoading -> {
                LoadingScreen(stringResource(R.string.auth_logging_in))
            }

            is AuthUiState.SignupLoading -> {
                LoadingScreen(stringResource(R.string.auth_signing_up))
            }

            is AuthUiState.Success -> {
                navViewModel.setUserInfo(
                    authUiState.auth.username,
                    authUiState.auth.id,
                    authUiState.auth.authToken
                )
                navController.navigate("Profile")
            }

            is AuthUiState.Registered -> {
                Text(
                    stringResource(R.string.auth_registered),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is AuthUiState.Error -> {
                Text(
                    text = stringResource(
                        if (authUiState.login) R.string.auth_login_error
                        else R.string.auth_register_error
                    ),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        when (authUiState) {
            is AuthUiState.Success, AuthUiState.Starting, AuthUiState.RefreshLoading -> {}
            else -> {
                Spacer(modifier = defaultPadding)

                AuthenticateInputs(
                    username = username,
                    password = password,
                    isAuthError = authUiState is AuthUiState.Error,
                    onUpdateUsername = { username = it },
                    onUpdatePassword = { password = it },
                )

                Spacer(modifier = defaultPadding)

                AuthenticateButtons(
                    username,
                    password,
                    { u: String, p: String -> authViewModel.checkCredentials(u, p) },
                    { u: String, p: String -> authViewModel.registerUser(u, p) }
                )
            }
        }
    }
}
/**
 * Composable function that displays login and registration buttons.
 *
 * This function renders two buttons: one for logging in and another for signing up. Each button
 * is enabled only when both the username and password fields are not empty. This function handles
 * user actions for login and registration by invoking callback functions when the respective button
 * is pressed, passing the username and password as arguments.
 *
 * @param username A string representing the username input by the user. The buttons are active only if this is non-empty.
 * @param password A string representing the password input by the user. The buttons are active only if this is non-empty.
 * @param onLogin A lambda function that is invoked with username and password as parameters when the login button is clicked.
 * @param onSignup A lambda function that is invoked with username and password as parameters when the signup button is clicked.
 */
@Composable
fun AuthenticateButtons(
    username: String,
    password: String,
    onLogin: (String, String) -> Unit,
    onSignup: (String, String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                onLogin(username, password)
            },
            enabled = username.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(stringResource(R.string.auth_login))
        }

        Button(
            onClick = {
                onSignup(username, password)
            },
            enabled = username.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(stringResource(R.string.auth_register))
        }
    }
}
/**
 * Composable function that renders input fields for username and password.
 *
 * This function displays two outlined text fields: one for entering the username and another for entering the password.
 * Each field will update its respective state variable when the text changes, using the provided callback functions.
 * Additionally, both input fields can visually indicate an authentication error through the {@code isError} parameter,
 * which alters the appearance of the text fields to show an error state.
 *
 * @param username The current value of the username input field.
 * @param password The current value of the password input field.
 * @param isAuthError Optional boolean that, when true, indicates that there is an authentication error, affecting
 *                    the visual state of the text fields. Defaults to false.
 * @param onUpdateUsername A lambda function that updates the username state variable on text change.
 * @param onUpdatePassword A lambda function that updates the password state variable on text change.
 */
@Composable
fun AuthenticateInputs(
    username: String,
    password: String,
    isAuthError: Boolean = false,
    onUpdateUsername: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUpdateUsername,
        singleLine = true,
        placeholder = { Text(stringResource(R.string.username)) },
        isError = isAuthError
    )

    Spacer(modifier = Modifier.padding(8.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onUpdatePassword,
        singleLine = true,
        placeholder = { Text(stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = isAuthError
    )
}