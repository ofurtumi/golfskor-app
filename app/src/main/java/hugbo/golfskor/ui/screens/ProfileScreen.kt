package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hugbo.golfskor.ui.NavigationMenu
import hugbo.golfskor.ui.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationMenu("Profile", navController)
        }
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Home Screen", fontSize = 54.sp)

            Spacer(modifier = Modifier.padding(16.dp))

            Text(text = "Username: ${profileUiState.user.getUsername()}", fontSize = 24.sp)
            Text(text = "Password: ${profileUiState.user.getToken()}", fontSize = 24.sp)
        }

        Button(onClick = { profileViewModel.logUserinfo() }) {
            Text("Log Userinfo")
        }
    }
}