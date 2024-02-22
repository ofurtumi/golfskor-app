package hugbo.golfskor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.R
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.AuthUiState
import hugbo.golfskor.ui.viewModels.AuthenticateViewModel


@Composable
fun AuthenticateScreen(
    navController: NavHostController,
    authViewModel: AuthenticateViewModel = viewModel(),
) {
    // val authUiState by authViewModel.uiState.collectAsState()
    val authUiState = authViewModel.authUiState
    val appName = stringResource(R.string.app_name)
    val defaultPadding = Modifier.padding(16.dp, 8.dp)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = appName, fontSize = 54.sp)

        Spacer(modifier = defaultPadding)

        when (authUiState) {
            is AuthUiState.Loading -> {
                Text(
                    stringResource(R.string.auth_please_login),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is AuthUiState.Success -> {
                navController.navigate("Profile/${authUiState.auth.username}/${authUiState.auth.authToken}")
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
                    text = stringResource(R.string.auth_error),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isAuthError
    )
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DefaultPreview() {
    GolfskorTheme {
        Surface {
            AuthenticateScreen(navController = rememberNavController())
        }
    }
}