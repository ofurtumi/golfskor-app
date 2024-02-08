package hugbo.golfskor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                // A surface container using the 'background' color from the theme
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

@Preview(showBackground = true)
@Composable
fun GolfRound(round: Round = Round(), header: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .padding(16.dp, 8.dp)
    ) {
        if (header) {
            val style = TextStyle(fontWeight = FontWeight.Bold)
            Text(style = style, text = "Username")
            Text(style = style, text = "Course")
            Text(style = style, text = "Score")
        } else {
            Text("@${round.getUsername()}")
            Text(round.getCourseName())
            Text(round.getScore().toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GolfRoundList (rounds: List<Round> = listOf(Round(username = "Tester 1"), Round(username = "Tester 2"), Round(username = "Tester 3"))) {
    Column {
        GolfRound(header = true)
        for (round in rounds) {
            Divider(color = MaterialTheme.colorScheme.onSurface, thickness = 1.dp, modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 1.dp))
            GolfRound(round)
        }
    }
}