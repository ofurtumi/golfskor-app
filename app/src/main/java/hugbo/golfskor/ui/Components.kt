package hugbo.golfskor.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.ui.theme.GolfskorTheme

@Composable
fun NavigationMenu(current: Int) {
    val navController = rememberNavController()
    Divider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        if (current == 0) {
            Button(onClick = { /*TODO*/ }) {
                Text("Courses")
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("Profile")
            }
        } else {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("Courses")
            }
            Button(onClick = { /*TODO*/ }) {
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
            NavigationMenu(0)
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
            NavigationMenu(1)
        }
    }
}