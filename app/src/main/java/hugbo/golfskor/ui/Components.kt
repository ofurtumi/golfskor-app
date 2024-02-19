package hugbo.golfskor.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.R
import hugbo.golfskor.entities.Round
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.ProfileViewModel

@Composable
fun NavigationMenu(current: String = "Courses", navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .fillMaxWidth(),

    ) {
        if (current == "Courses") {
            Button(onClick = { /*Todo*/ }) {
                Text("Courses")
            }
            OutlinedButton(onClick = { navController.navigate("Profile") }) {
                Text("Profile")
            }
        } else {
            OutlinedButton(onClick = { navController.navigate("Courses") }) {
                Text("Courses")
            }
            Button(onClick = { /*Todo*/ }) {
                Text("Profile")
            }
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
fun NavigationMenuPreviewCourses() {
    GolfskorTheme {
        Surface {
            NavigationMenu(navController = rememberNavController())
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
fun NavigationMenuPreviewProfile() {
    GolfskorTheme {
        Surface {
            NavigationMenu("Profile", navController = rememberNavController())
        }
    }
}


@Composable
fun GolfRound(round: Round = Round()) {
    GolfskorTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TextCollection(
                strings = listOf(
                    round.getUsername(),
                    round.getCourseName(),
                    round.getScore().toString()
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
    color: Color = Color.Unspecified) {
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
fun GolfRoundList (
    rounds: List<Round> = listOf(
        Round(username = "Tester 1"),
        Round(username = "Tester 2"),
        Round(username = "Tester 3")
    )) {
    LazyColumn {
        item {
            GolfRoundHeader(listOf("Username", "Course", "Score"))
        }
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