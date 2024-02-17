package hugbo.golfskor.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import hugbo.golfskor.ui.GolfRoundHeader
import hugbo.golfskor.ui.GolfRoundList
import hugbo.golfskor.ui.Line
import hugbo.golfskor.ui.NavigationMenu
import hugbo.golfskor.ui.ProfileGolfRound
import hugbo.golfskor.ui.ProfileGolfRoundList
import hugbo.golfskor.ui.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    profileViewModel: ProfileViewModel = viewModel(),
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Forgjöf: ${profileUiState.handicap}", fontSize = 54.sp)

        Spacer(modifier = Modifier.padding(16.dp))

        Text(text = "Username: ${profileUiState.username}", fontSize = 24.sp)
        Text(text = "Password: ${profileUiState.authToken}", fontSize = 24.sp)

        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = { profileViewModel.addRound() }) {
            Text("Log Userinfo")
        }

        Spacer(modifier = Modifier.padding(16.dp))

        ProfileGolfRoundList(
            rounds = profileUiState.rounds,
            editFun = { id -> profileViewModel.editRound(id) },
            deleteFun = { id -> profileViewModel.deleteRound(id) }
        )
    }
}