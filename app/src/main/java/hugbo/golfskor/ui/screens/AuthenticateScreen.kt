package hugbo.golfskor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hugbo.golfskor.R
import hugbo.golfskor.ui.theme.GolfskorTheme

@Composable
fun AuthenticateScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Golfskor", fontSize = 54.sp)

        Spacer(modifier = Modifier.padding(16.dp))

        val pleaseLogin = stringResource(R.string.auth_please_login)
        Text(pleaseLogin, fontSize = 24.sp, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.padding(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") }
        )
        TextField(
            value = password,
            onValueChange = { password= it },
            placeholder = { Text("Password") }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .defaultMinSize(minWidth = 300.dp, minHeight = 1.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate("Profile/$username/$password") },
                enabled = username.isNotEmpty()
            ) {
                val login = stringResource(R.string.auth_login)
                Text(login)
            }
            Button(
                onClick = { navController.navigate("Profile/$username/$password") },
                enabled = username.isNotEmpty()
            ) {
            val register = stringResource(R.string.auth_register)
            Text(register)
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
fun DefaultPreview() {
    GolfskorTheme {
        Surface {
            AuthenticateScreen(navController = rememberNavController())
        }
    }
}