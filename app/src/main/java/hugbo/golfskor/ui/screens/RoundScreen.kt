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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.NavViewModel
import hugbo.golfskor.ui.viewModels.RoundUiState
import hugbo.golfskor.ui.viewModels.RoundViewModel

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
                LoadingScreen()
                roundViewModel.getRoundOfType(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.authToken,
                    roundViewModel.roundType,
                    roundViewModel.id
                )
            }


            is RoundUiState.NewRound -> {
                var holes by remember(roundUiState.holes) { mutableStateOf(roundUiState.holes) }

                ChooseHoles(holes, onHoleChange = { holes = it })
                {
                    Button(onClick = {
                        roundViewModel.postRound(
                            holes,
                            navViewModel.navUiState.userId,
                            navViewModel.navUiState.authToken
                        )
                        navController.navigate("Profile")
                    }) {
                        Text("Create Round")
                    }
                }
            }

            is RoundUiState.OldRound -> {
                var holes by remember(roundUiState.round.holes) { mutableStateOf(roundUiState.round.holes) }

                ChooseHoles(holes, onHoleChange = { holes = it }) {

                    Button(onClick = {
                        roundViewModel.updateRound(
                            holes,
                            roundUiState.round.id,
                            navViewModel.navUiState.userId,
                            navViewModel.navUiState.authToken
                        )
                        navController.navigate("Profile")
                    }) {
                        Text("Create Round")
                    }
                }
            }

            is RoundUiState.Success -> LoadingScreen()
            is RoundUiState.Error -> LoadingScreen()
        }
    }
}

@Composable
fun ChooseHoles(
    holes: List<Int>,
    onHoleChange: (List<Int>) -> Unit,
    content: @Composable () -> Unit
) {
    LazyColumn {
        itemsIndexed(holes) { i, hole ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
                    "Hole ${i + 1}: $hole",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
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
            content()
        }
    }
}

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
fun UpdateHoleButtonPreview() {
    GolfskorTheme {
        Surface {
            UpdateHoleButton("+1") {}
        }
    }
}