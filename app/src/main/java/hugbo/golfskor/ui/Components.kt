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

/**
 * Displays the details of a single golf round using a predefined theme.
 *
 * This function is designed to present the information of a golf round within a themed Surface component.
 * It encapsulates the round's username, course name, and score within a TextCollection composable.
 *
 * @param round An instance of ApiRound, which contains data about a golf round including the username of the
 *              player, the name of the course, and the score. Defaults to a preview round if not provided.
 */
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

/**
 * Composable function that displays a header for golf round information.
 *
 * This function is designed to present header labels for sections of a golf app interface, such as a list of golf rounds.
 * It utilizes the `TextCollection` composable to arrange the header strings horizontally.
 *
 * @param strings A list of strings that represent different header titles (e.g., "Course", "Holes", "Score").
 *                These are displayed as key identifiers for data columns in a table or list.
 */
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

/**
 * Composable function that displays a collection of text elements in a row.
 *
 * This function creates a horizontal row of text elements, spaced evenly across the available width.
 *
 * @param strings A list of strings that are to be displayed as individual text elements within the row.
 * @param modifier A `Modifier` applied to each text element in the collection. It can be used to adjust the layout,
 *                 appearance, or add behavior to the text elements.
 * @param style A `TextStyle` that will be applied to all text elements in the collection. It allows for consistent
 *              text formatting across all items.
 * @param color A `Color` applied to the text elements. If unspecified, the text color defaults to the ambient
 *              color from the theme.
 */
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

/**
 * Composable function that creates a horizontal line divider.
 *
 * This function provides a simple, styled horizontal line that can be used to separate content visually
 * in the user interface. The line's color is set to the secondary color of the current theme.
 */
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

/**
 * Composable function that displays a list of golf rounds in a lazy loading column.
 *
 * This function uses a `LazyColumn` to efficiently render a list of golf rounds, making it suitable for handling
 * potentially large data sets by only rendering items that are currently visible on the screen. Each round is
 * represented by the `GolfRound` composable, and is separated by a line for clear visual distinction.
 *
 * @param rounds A list of `ApiRound` objects representing the data for each golf round. Defaults to a list containing
 *               three preview rounds if not provided.
 */
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

/**
 * Composable function that displays a list of golf courses and associated rounds.
 *
 * This function presents detailed information for each course retrieved from the state, including the course name,
 * the total par, and the user's adjusted handicap on that course. It also displays options for starting new rounds
 * of 9 or 18 holes, allowing users to navigate to the corresponding screens for each course.
 *
 * Each course entry includes a list of recent rounds played on that course, showing player names, scores, and hole details.
 * The hole details are formatted to fit nicely within the UI, splitting into two lines if the number exceeds nine holes.
 *
 * The layout uses a `LazyColumn` to handle potentially large lists of courses efficiently, only rendering visible items.
 *
 * @param state The UI state containing the list of courses and their details, as retrieved from the application's backend.
 * @param navController Navigation controller for handling screen transitions, allowing users to start new rounds.
 * @param userHandicap The user's golf handicap, used to calculate adjusted scores for each course.
 */
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

/**
 * Composable function that displays an error message.
 *
 * This function creates a simple text-based user interface component that displays an error message. The text is styled
 * using the error color defined in the application's color scheme, which helps to distinguish the error message visually
 * from other text in the application.
 *
 * @param message The error message to be displayed. This message is prefixed with "Error: " to clearly indicate its nature.
 */
@Composable
fun ErrorScreen(message: String) {
    Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
}

/**
 * Composable function that displays a loading screen with a customizable message and a progress indicator.
 *
 * This function creates a user interface component suitable for indicating that data fetching or processing
 * is in progress. It centers a text message and a horizontal progress bar within the screen. The text message
 * can be customized, and the progress bar's colors are derived from the current theme, enhancing the visual
 * integration with the rest of the application.
 *
 * @param message The message to be displayed above the progress bar. Defaults to "Sæki gögn..." (Fetching data... in Icelandic).
 */
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