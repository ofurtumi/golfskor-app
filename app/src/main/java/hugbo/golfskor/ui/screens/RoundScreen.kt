package hugbo.golfskor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
            is RoundUiState.Loading -> LoadingScreen()

            is RoundUiState.NewRound -> {
                var holes by remember(roundUiState.holes) { mutableStateOf(roundUiState.holes) }

                ChooseHoles(holes, onHoleChange = { holes = it })

                Button(onClick = {
                    roundViewModel.postRound(holes)
                    navController.navigate("Profile/${roundUiState.username}/${roundUiState.authToken}")
                }) {
                    Text("Create Round")
                }
            }

            is RoundUiState.OldRound -> {
                Text(roundUiState.username)
                Text(roundUiState.authToken)
                Text(roundUiState.round.toString())
            }

            is RoundUiState.Error -> ErrorScreen(roundUiState.message)
        }
    }
}

@Composable
fun ChooseHoles(holes: List<Int>, onHoleChange: (List<Int>) -> Unit) {
    LazyColumn {
        itemsIndexed(holes) { i, hole ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i && hole > 1) hole - 1 else old })
                }) { Text("-1") }

                Button(onClick = {
                    onHoleChange(
                        holes.withIndex()
                            .map { (index, old) -> if (index == i && hole > 3) hole - 3 else old })
                }) { Text("-3") }

                Text(
                    "Hole ${i + 1}: $hole",
                )

                Button(onClick = {
                    onHoleChange(
                        holes.withIndex().map { (index, old) -> if (index == i) hole + 3 else old })
                }) { Text("+3") }

                Button(onClick = {
                    onHoleChange(
                        holes.withIndex().map { (index, old) -> if (index == i) hole + 1 else old })
                }) { Text("+1") }
            }
        }
    }
}
