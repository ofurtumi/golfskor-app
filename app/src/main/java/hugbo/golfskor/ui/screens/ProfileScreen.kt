package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hugbo.golfskor.R
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.ui.GolfRoundHeader
import hugbo.golfskor.ui.Line
import hugbo.golfskor.ui.TextCollection
import hugbo.golfskor.ui.viewModels.NavViewModel
import hugbo.golfskor.ui.viewModels.ProfileUiState
import hugbo.golfskor.ui.viewModels.ProfileViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    navViewModel: NavViewModel,
    profileViewModel: ProfileViewModel = viewModel(),
) {
    val profileUiState = profileViewModel.profileUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when (profileUiState) {
            is ProfileUiState.Loading -> {
                Text(text = "Sæki gögn", fontSize = 24.sp)
                profileViewModel.getProfileRounds(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.authToken
                )
            }

            is ProfileUiState.Success -> {
                Text(text = "Forgjöf: ${roundHandicap(profileUiState.handicap)}", fontSize = 54.sp)
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = "Username: ${profileUiState.username}", fontSize = 24.sp)
                Spacer(modifier = Modifier.padding(16.dp))
                ProfileGolfRoundList(
                    rounds = profileUiState.rounds,
                    editFun = { /* TODO */ },
                    deleteFun = { /* TODO */ },
                )
            }

            is ProfileUiState.Error -> {
                Text(
                    text = "Sæki holur",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun roundHandicap(handicap: Double): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(handicap).toString()
}

@Composable
fun ProfileGolfRound(
    round: ApiRound,
    editAction: () -> Unit = { },
    deleteAction: () -> Unit = { },
) {
    Column {
        TextCollection(
            listOf(
                round.courseName,
                round.holes.joinToString(", "),
                round.score.toString(),
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = deleteAction
            ) {
                Text(stringResource(R.string.delete))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = editAction
            ) { Text(stringResource(R.string.edit)) }
        }
    }
}


@Composable
fun ProfileGolfRoundList(
    rounds: List<ApiRound>,
    editFun: (Int) -> Unit = { },
    deleteFun: (Int) -> Unit = { }
) {
    LazyColumn {
        item { GolfRoundHeader(listOf("Course", "Holes", "Score")) }
        items(rounds) { round ->
            Line()
            ProfileGolfRound(
                round,
                editAction = { editFun(round.id) },
                deleteAction = { deleteFun(round.id) }
            )
        }
        item { Line() }
    }
}