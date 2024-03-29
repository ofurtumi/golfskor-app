package hugbo.golfskor.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.entities.previewCourse
import hugbo.golfskor.entities.previewRound
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.CourseUiState

@Composable
fun GolfRound(
    round: ApiRound = previewRound()
) {
    GolfskorTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {

            TextCollection(
                strings = listOf(
                    round.username,
                    round.courseName,
                    round.score.toString()
                )

            )
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
fun GolfRoundPreview() {
    GolfskorTheme {
        Surface {
            GolfRound()
        }
    }
}

@Composable
fun GolfRoundHeader(strings: List<String>) {
    TextCollection(
        strings,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun GolfRoundHeaderPreview() {
    GolfskorTheme {
        Surface {
            GolfRoundHeader(listOf("Username", "Course", "Score"))
        }
    }
}

@Composable
fun TextCollection(
    strings: List<String>,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    color: Color = Color.Unspecified
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        for (string in strings) {
            Text(text = string, modifier = modifier, style = style, color = color)
        }
    }
}

@Composable
fun Line() {
    Divider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .padding(0.dp, 8.dp)
    )
}

@Composable
fun GolfRoundList(
    rounds: List<ApiRound> = List(3) { previewRound() }
) {
    LazyColumn {
        items(rounds) { round ->
            Line()
            GolfRound(round)
        }
        item {
            Line()
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
fun GolfRoundListPreview() {
    GolfskorTheme {
        Surface {
            GolfRoundList()
        }
    }
}

@Composable
fun GolfCourseList(
    state: CourseUiState.Success,
    navController: NavController,
    userHandicap: Double
) {
    LazyColumn {
        items(state.courses) { course ->
            Text(
                text = course.courseName,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Par: ${course.coursePars.sum()}",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Forgjöfin þín er ${(course.coursePars.sum() + userHandicap).toInt()}",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { navController.navigate("Rounds/new/${course.id}/${course.courseName}") }) {
                    Text(text = "9 Holur")
                }
                Button(onClick = { navController.navigate("Rounds/big/${course.id}/${course.courseName}") }) {
                    Text(text = "18 Holur")
                }
            }
            GolfRoundHeader(strings = listOf("Spilari", "Holur", "Skor"))
            for (round in course.rounds) {
                var holes = round.holes.joinToString(", ")
                if (round.holes.size > 9) {
                    holes =
                        round.holes.subList(0, 9).joinToString(", ") +
                                "\n" +
                                round.holes.subList(9, round.holes.size).joinToString(", ")
                }
                Line()
                TextCollection(
                    strings = listOf(
                        round.username,
                        holes,
                        round.score.toString()
                    )

                )
            }
            Line()
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
fun GolfCourseListPreview() {
    GolfskorTheme {
        Surface {
            GolfCourseList(
                state = CourseUiState.Success(
                    courses = List(2) { previewCourse() }
                ),
                navController = rememberNavController(),
                userHandicap = 54.0
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
}

@Composable
fun LoadingScreen(message: String = "Sæki gögn...") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, fontSize = 24.sp)
        LinearProgressIndicator(
            modifier = Modifier.width(200.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
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
fun LoadingScreenPreview() {
    GolfskorTheme {
        Surface {
            LoadingScreen()
        }
    }
}