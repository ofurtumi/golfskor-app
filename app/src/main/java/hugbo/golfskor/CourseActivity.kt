package hugbo.golfskor

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hugbo.golfskor.entities.Round
import hugbo.golfskor.ui.theme.GolfskorTheme

class CourseActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GolfskorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GolfRoundList()
                }
            }
        }
    }
}

@Composable
fun GolfRound(round: Round = Round(), header: Boolean = false) {
    GolfskorTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                if (header) {
                    TextCollection(
                        strings = listOf("Username", "Course", "Score"),
                        style = TextStyle(fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                    )
                } else {
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
            GolfRound(header = true)
        }
    }
}

@Composable
fun TextCollection(
    strings: List<String>,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    color: Color = Color.Unspecified) {
    for (string in strings) {
        Text(text = string, modifier = modifier, style = style, color = color)
    }
}

@Composable
fun GolfRoundList (
    rounds: List<Round> = listOf(
        Round(username = "Tester 1"),
        Round(username = "Tester 2"),
        Round(username = "Tester 3"))) {
    LazyColumn {
        item {
            GolfRound(header = true)
        }
        items(rounds) { round ->
            Divider(
                color = MaterialTheme.colorScheme.secondary,
                thickness = 1.dp,
                modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            )
            GolfRound(round)
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
