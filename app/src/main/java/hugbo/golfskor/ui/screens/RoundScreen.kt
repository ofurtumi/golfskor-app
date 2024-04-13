package hugbo.golfskor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hugbo.golfskor.R
import hugbo.golfskor.ui.LoadingScreen
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.NavViewModel
import hugbo.golfskor.ui.viewModels.RoundUiState
import hugbo.golfskor.ui.viewModels.RoundViewModel

val paddingValues = PaddingValues(16.dp, 4.dp)

/**
 * Composable function that provides the user interface for managing golf rounds.
 *
 * This function presents different UI states related to golf rounds, controlled through the {@code roundViewModel}.
 * The UI displays based on the state of {@code roundUiState} which includes loading, creating a new round, viewing an
 * old round, success, and error states. Each state provides relevant UI elements and functionality:
 *
 * - In the 'Loading' state, it fetches round data based on user credentials and the specified round type.
 * - The 'NewRound' state allows users to create a new round, inputting data such as holes played, and submit it.
 * - The 'OldRound' state allows users to view and update data for an existing round.
 * - The 'Success' state might show a completion or transition screen (currently loads indefinitely).
 * - The 'Error' state shows an error screen (currently loads indefinitely).
 *
 * @param innerPadding Padding to apply inside the screen, typically provided by the scaffold or parent composable.
 * @param navController Controller for navigating between screens.
 * @param navViewModel ViewModel that holds navigation and user session data.
 * @param roundViewModel ViewModel managing round data and state. It is responsible for fetching and updating rounds.
 */
@Composable
fun RoundScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    navViewModel: NavViewModel,
    roundViewModel: RoundViewModel = viewModel()
) {
    val roundUiState = roundViewModel.roundUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when (roundUiState) {
            is RoundUiState.Loading -> {
                LoadingScreen(stringResource(R.string.fetching_rounds))
                roundViewModel.getRoundOfType(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.authToken,
                    roundViewModel.roundType,
                    roundViewModel.id
                )
            }


            is RoundUiState.NewRound -> {
                var holes by remember(roundUiState.holes) { mutableStateOf(roundUiState.holes) }
                Text(
                    roundUiState.courseName,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                ChooseHoles(holes, { holes = it }, roundUiState.buttonText) {
                    roundViewModel.postRound(
                        holes,
                        navViewModel.navUiState.userId,
                        navViewModel.navUiState.authToken
                    )
                    navController.navigate("Profile")
                }
            }

            is RoundUiState.OldRound -> {
                var holes by remember(roundUiState.round.holes) { mutableStateOf(roundUiState.round.holes) }

                Text(
                    roundUiState.courseName,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                ChooseHoles(holes, { holes = it }, roundUiState.buttonText) {
                    roundViewModel.updateRound(
                        holes,
                        roundUiState.round.id,
                        navViewModel.navUiState.userId,
                        navViewModel.navUiState.authToken
                    )
                    navController.navigate("Profile")
                }

            }

            is RoundUiState.Success -> LoadingScreen()
            is RoundUiState.Error -> LoadingScreen()
        }
    }
}

/**
 * Composable function that displays a list of golf holes and allows for score updates.
 *
 * This function generates a dynamic list of holes with the current scores and provides buttons to increment
 * or decrement the scores for each hole. Users can adjust scores by specified amounts (-3, -1, +1, +3).
 * The list is displayed in a {@code LazyColumn}, ensuring efficient rendering even with a large number of holes.
 * Each hole is represented in a row with buttons for adjusting the score and the current score displayed prominently.
 *
 * The function also includes a submit button at the end of the list, which when clicked, executes the onSubmit callback,
 * used to finalize the score entry to a backend.
 *
 * @param holes A list of integers where each integer represents the current score of a golf hole.
 * @param onHoleChange A callback function that updates the list of scores. It is triggered by the score adjustment buttons.
 * @param submitText Text to display on the submit button at the end of the list.
 * @param onSubmit A lambda function that is called when the submit button is pressed.
 */
@Composable
fun ChooseHoles(
    holes: List<Int>,
    onHoleChange: (List<Int>) -> Unit,
    submitText: String,
    onSubmit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        itemsIndexed(holes) { i, hole ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${i + 1}.",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                UpdateHoleButton(
                    "-1"
                ) {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i && hole > 1) hole - 1 else old })
                }

                UpdateHoleButton(
                    "-3"
                ) {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i && hole > 3) hole - 3 else old })
                }

                Text(
                    "$hole",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                UpdateHoleButton(
                    "+3"
                ) {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i) hole + 3 else old })
                }

                UpdateHoleButton(
                    "+1"
                ) {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i) hole + 1 else old })
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = submitText,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}

/**
 * Composable function that creates a styled button for updating golf hole scores.
 *
 * @param text The text displayed on the button, indicating the action (e.g., "+1", "-1").
 * @param callBack A lambda function that is executed when the button is clicked, handling the specific increment
 *                 or decrement logic as required by the surrounding context.
 */
@Composable
fun UpdateHoleButton(text: String, callBack: () -> Unit) {
    OutlinedButton(
        onClick = callBack,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun ChooseHolesPreview() {
    GolfskorTheme {
        Surface {
            ChooseHoles(
                holes = List(9) { 1 },
                onHoleChange = { },
                onSubmit = {},
                submitText = "Submit"
            )
        }
    }
}