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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import hugbo.golfskor.ui.LoadingScreen
import hugbo.golfskor.ui.TextCollection
import hugbo.golfskor.ui.viewModels.NavViewModel
import hugbo.golfskor.ui.viewModels.ProfileUiState
import hugbo.golfskor.ui.viewModels.ProfileViewModel
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Composable function that displays the user profile screen in a golf application.
 *
 * This screen manages various UI states related to the user profile, such as loading, success, deleting rounds,
 * error, and signed out states. It provides information about the user, including handicap, weather conditions,
 * and a list of golf rounds. Depending on the state of {@code profileUiState}, different components and actions
 * are displayed:
 *
 * - 'Loading' state triggers fetching of profile data.
 * - 'Deleting' state shows a confirmation message and refreshes the list of rounds.
 * - 'Success' state displays detailed user information and actions for logging out, editing, and deleting rounds.
 * - 'Error' state presents an error message.
 * - 'SignedOut' state redirects to the authentication screen.
 *
 * @param innerPadding Padding to apply inside the screen, provided by the scaffold or parent composable.
 * @param navController Controller for navigating between screens.
 * @param navViewModel ViewModel that holds navigation and user session data.
 * @param profileViewModel ViewModel for managing profile data and state. It handles operations like fetching,
 *                         deleting rounds, and signing out.
 */
@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    navViewModel: NavViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {

    val profileUiState = profileViewModel.profileUiState
    val deleteAlertOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when (profileUiState) {
            is ProfileUiState.Loading -> {
                LoadingScreen(stringResource(R.string.fetching_data))
                profileViewModel.getProfileRounds(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.authToken
                )
            }

            is ProfileUiState.Deleting -> {
                Text(text = "Færslu eytt :)", fontSize = 24.sp)
                LaunchedEffect(key1 = Unit) {
                    delay(100)
                    profileViewModel.getProfileRounds(
                        navViewModel.navUiState.username,
                        navViewModel.navUiState.authToken
                    )
                }
            }

            is ProfileUiState.Success -> {
                when {
                    deleteAlertOpen.value -> {
                        AlertDialog(
                            icon = {
                                Icon(
                                    Icons.Filled.VerifiedUser,
                                    contentDescription = "Delete User Icon"
                                )
                            },
                            title = {
                                Text("Ertu viss um að þú viljir eyða notanda?")
                            },
                            text = {
                                Text("Þessa aðgerð er ekki hægt að afturkalla...")
                            },
                            onDismissRequest = {
                                deleteAlertOpen.value = false
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        deleteAlertOpen.value = false
                                        profileViewModel.deleteUser()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ),
                                ) {
                                    Text("Eyða")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        deleteAlertOpen.value = false
                                    },
                                    modifier = Modifier
                                ) {
                                    Text("Hætta við")
                                }
                            }
                        )
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Spilari: ${profileUiState.username}", fontSize = 24.sp)
                    Button(
                        onClick = { deleteAlertOpen.value = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eyða notanda")
                    }
                    Button(
                        onClick = { profileViewModel.signOut() },
                    ) {
                        Text("Skrá út")
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Forgjöf: ${roundHandicap(profileUiState.handicap)}",
                        fontSize = 24.sp
                    )
                    Text("Hiti: ${profileUiState.heat} °C", fontSize = 24.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vindur: ${profileUiState.wind} m/s", fontSize = 24.sp)
                    Text("Vindátt: ${profileUiState.direction}", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.padding(16.dp))
                ProfileGolfRoundList(
                    rounds = profileUiState.rounds,
                    editFun = { roundId, courseName -> navController.navigate("Rounds/old/${roundId}/${courseName}") },
                    deleteFun = { roundId ->
                        profileViewModel.deleteRound(
                            roundId,
                            navViewModel.navUiState.userId,
                            navViewModel.navUiState.authToken
                        )
                    }
                )
            }

            is ProfileUiState.Error -> {
                Text(
                    text = profileUiState.message,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is ProfileUiState.SignedOut -> {
                navController.navigate("Authenticate")
            }
        }
    }
}

/**
 * Formats and rounds a golf handicap value to one decimal place.
 *
 * This function takes a double representing a golf handicap and rounds it using a ceiling rounding mode,
 * which rounds towards the next higher number. The function uses the {@link DecimalFormat} class to
 * format the number to one decimal place.
 *
 * @param handicap The golf handicap to be rounded and formatted.
 * @return A string representing the formatted golf handicap rounded to one decimal place.
 */
private fun roundHandicap(handicap: Double): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(handicap).toString()
}

/**
 * Composable function that displays detailed information about a single golf round.
 *
 * This function presents information about a golf round, such as the course name, a formatted list of holes
 * played, and the score. It handles rounds with a number of holes greater than nine by splitting the hole
 * information into two lines for better readability. It also provides buttons for editing and deleting the
 * round, enabling user interaction with each round item.
 *
 * @param round An instance of {@code ApiRound}, which contains the data for the golf round including course name,
 *              list of holes, and score.
 * @param editAction A lambda function that is executed when the user presses the edit button. Defaults to an empty lambda.
 * @param deleteAction A lambda function that is executed when the user presses the delete button. Defaults to an empty lambda.
 */
@Composable
fun ProfileGolfRound(
    round: ApiRound,
    editAction: () -> Unit = { },
    deleteAction: () -> Unit = { },
) {
    Column {
        var holes = round.holes.joinToString(", ")
        if (round.holes.size > 9) {
            holes =
                round.holes.subList(0, 9).joinToString(", ") +
                        "\n" +
                        round.holes.subList(9, round.holes.size).joinToString(", ")
        }
        TextCollection(
            listOf(
                round.courseName,
                holes,
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

/**
 * Composable function that renders a list of golf rounds using a lazy loading column.
 *
 * This function displays a scrollable list of golf rounds, where each round is represented by the `ProfileGolfRound`
 * composable. It includes a header that labels the columns as "Course", "Holes", and "Score", and a line separator
 * is placed between each round for clarity. The function is designed to handle potentially large lists efficiently
 * by using a `LazyColumn`, which only renders items that are currently visible on the screen.
 *
 * Each golf round in the list includes actions for editing and deleting. These actions are linked to provided
 * lambda functions, allowing external handling of these interactions (such as opening an edit screen or performing
 * a delete operation).
 *
 * @param rounds A list of {@code ApiRound} objects representing the data for each golf round.
 * @param editFun A function that is called when the edit button is pressed for a round. It accepts the round's id
 *                and course name as parameters.
 * @param deleteFun An optional function that is called when the delete button is pressed for a round, accepting
 *                  the round's id as a parameter. Defaults to an empty function if not provided.
 */
@Composable
fun ProfileGolfRoundList(
    rounds: List<ApiRound>,
    editFun: (Int, String) -> Unit,
    deleteFun: (Int) -> Unit = { }
) {
    LazyColumn {
        item { GolfRoundHeader(listOf("Course", "Holes", "Score")) }
        items(rounds) { round ->
            Line()
            ProfileGolfRound(
                round,
                editAction = { editFun(round.id, round.courseName) },
                deleteAction = { deleteFun(round.id) }
            )
        }
        item { Line() }
    }
}