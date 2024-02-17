package hugbo.golfskor.ui.screens

import android.content.res.Configuration
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import hugbo.golfskor.ui.viewModels.AuthenticateViewModel

@Composable
fun AuthenticateScreen(
    navController: NavHostController,
    authViewModel: AuthenticateViewModel = viewModel(),
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val appName = stringResource(R.string.app_name)
    val defaultPadding = Modifier.padding(16.dp, 8.dp)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = appName, fontSize = 54.sp)

        Spacer(modifier = defaultPadding)

        Text(
            stringResource(R.string.auth_please_login),
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = defaultPadding)

        AuthenticateInputs(
            username = authUiState.username,
            password = authUiState.password,
            isAuthError = authUiState.isAuthErr,
            onUpdateUsername = { authViewModel.updateUsername(it) },
            onUpdatePassword = { authViewModel.updatePassword(it) },
        )

        Spacer(modifier = defaultPadding)

        if (authUiState.message.isNotEmpty()) {
            Text(authUiState.message)
        }

        Spacer(modifier = defaultPadding)

        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (authViewModel.checkCredentials()) {
                        navController.navigate(
                            "Profile/${authUiState.username}/${authUiState.password}"
                        )
                    }
                },
                enabled = authUiState.username.isNotEmpty()
            ) {
                Text(stringResource(R.string.auth_login))
            }
            Button(
                onClick = {
                    authViewModel.createUser()
                },
                enabled = authUiState.username.isNotEmpty()
            ) {
                Text(stringResource(R.string.auth_register))
            }
        }
    }
}

@Composable
fun AuthenticateInputs (
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