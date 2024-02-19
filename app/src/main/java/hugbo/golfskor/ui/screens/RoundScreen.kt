package hugbo.golfskor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hugbo.golfskor.entities.Round
import hugbo.golfskor.entities.User

@Preview
@Composable
fun RoundScreen(round: Round = Round(), user: User = User()) {
    var holes by remember(round.getHoles()) { mutableStateOf(round.getHoles()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for ((i, hole) in holes.withIndex()) {
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
                    holes = holes.withIndex()
                        .map { (index, old) -> if (index == i && hole > 1) hole - 1 else old }
                }) { Text("-1") }

                Button(onClick = {
                    holes = holes.withIndex()
                        .map { (index, old) -> if (index == i && hole > 3) hole - 3 else old }
                }) { Text("-3") }

                Text(
                    "Hole ${i + 1}: $hole",
                )

                Button(onClick = {
                    holes =
                        holes.withIndex().map { (index, old) -> if (index == i) hole + 3 else old }
                }) { Text("+3") }

                Button(onClick = {
                    holes =
                        holes.withIndex().map { (index, old) -> if (index == i) hole + 1 else old }
                }) { Text("+1") }
            }

        }
    }
}
