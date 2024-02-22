package hugbo.golfskor.ui.screens

import android.content.res.Configuration
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hugbo.golfskor.R
import hugbo.golfskor.entities.Round
import hugbo.golfskor.ui.GolfRoundHeader
import hugbo.golfskor.ui.Line
import hugbo.golfskor.ui.TextCollection
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.ProfileViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

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
        Text(text = "Forgjöf: ${roundHandicap(profileUiState.handicap)}", fontSize = 54.sp)

        Spacer(modifier = Modifier.padding(16.dp))

        Text(text = "Username: ${profileUiState.username}", fontSize = 24.sp)

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

private fun roundHandicap(handicap: Double): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(handicap).toString()
}

@Composable
fun ProfileGolfRound(
    round: Round = Round(),
    editAction: () -> Unit = { },
    deleteAction: () -> Unit = { },
) {
    Column {
        TextCollection(
            listOf(
                round.getCourseName(),
                round.getHoleString(),
                round.getScore().toString(),
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun ProfileGolfRoundPreview() {
    GolfskorTheme {
        Surface {
            ProfileGolfRound()
        }
    }
}

@Composable
fun ProfileGolfRoundList(
    rounds: List<Round>,
    editFun: (Int) -> Unit = { },
    deleteFun: (Int) -> Unit = { }
) {
    LazyColumn {
        item { GolfRoundHeader(listOf("Course", "Holes", "Score")) }
        items(rounds) { round ->
            Line()
            ProfileGolfRound(
                round,
                editAction = { editFun(round.getId()) },
                deleteAction = { deleteFun(round.getId()) }
            )
        }
        item { Line() }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun ProfileGolfRoundListPreview() {
    GolfskorTheme {
        Surface {
            ProfileGolfRoundList(
                listOf(
                    Round(id = 1, username = "Tester 1"),
                    Round(id = 2, username = "Tester 2"),
                    Round(id = 3, username = "Tester 3")
                )
            )
        }
    }
}