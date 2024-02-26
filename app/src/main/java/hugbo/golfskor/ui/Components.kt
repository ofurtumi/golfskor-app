package hugbo.golfskor.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.ui.theme.GolfskorTheme
import hugbo.golfskor.ui.viewModels.CourseUiState
import hugbo.golfskor.ui.viewModels.NavUiState

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
fun GolfRound(
    round: ApiRound = ApiRound(
        1,
        "Test Course",
        "Tester",
        listOf(1, 2, 3, 1, 2, 3, 1, 2, 3),
        18
    )
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
    rounds: List<ApiRound> = listOf(
        ApiRound(1, "Test Course", "Tester 1", listOf(1, 2, 3, 1, 2, 3, 1, 2, 3), 18),
        ApiRound(1, "Test Course", "Tester 2", listOf(1, 2, 3, 1, 2, 3, 1, 2, 3), 18),
        ApiRound(1, "Test Course", "Tester 3", listOf(1, 2, 3, 1, 2, 3, 1, 2, 3), 18),
    )
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
    navUiState: NavUiState
) {
    LazyColumn {
        items(state.courses) { course ->
            Row {
                Text(text = course.courseName)
                Button(onClick = { navController.navigate("Rounds/new/${course.id}/${navUiState.username}/${navUiState.userId}/${navUiState.authToken}") }) {
                    Text(text = "9 Holur")
                }
                Button(onClick = { navController.navigate("Rounds/newBig/${course.id}/${navUiState.username}/${navUiState.userId}/${navUiState.authToken}") }) {
                    Text(text = "18 Holur")
                }
            }
            GolfRoundHeader(strings = listOf("Spilari", "Holur", "Skor"))
            for (round in course.rounds) {
                Line()
                TextCollection(
                    strings = listOf(
                        round.username,
                        round.holes.joinToString(", "),
                        round.score.toString()
                    )

                )
            }
            Line()
        }
    }
}

@Composable
fun BottomNav(links: List<String>, navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        links.forEachIndexed { index, screen ->
            val icon = if (index == 0) {
                Icons.Filled.LocationOn
            } else {
                Icons.Filled.AccountCircle
            }
            BottomNavigationItem(
                modifier = Modifier.background(color = androidx.compose.material.MaterialTheme.colors.primary),
                icon = { Icon(imageVector = icon, contentDescription = icon.name) },
                selected = currentDestination?.hierarchy?.any { it.route == screen } == true,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}