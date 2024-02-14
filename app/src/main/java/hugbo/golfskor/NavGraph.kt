package hugbo.golfskor

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hugbo.golfskor.ui.screens.AuthenticateScreen
import hugbo.golfskor.ui.screens.CoursesScreen
import hugbo.golfskor.ui.screens.ProfileScreen
import kotlin.system.exitProcess

@Composable
fun Nav() {
    val navController = rememberNavController()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "Authenticate") {
        composable(route = "Authenticate") {
            AuthenticateScreen(navController = navController)
        }
        composable(route = "Courses") { CoursesScreen(navController) }
        composable(
            route = "Profile/{username}/{password}",
            arguments = listOf(
                navArgument(name = "username") {
                    type = NavType.StringType
                },
                navArgument(name = "password") {
                    type = NavType.StringType
                }
            )
        ) {
            backStackEntry ->
            BackHandler(false) {
                Log.i("myTag", "Back button pressed")
            }

            if (username == "" && password == "") {
                username = backStackEntry.arguments?.getString("username") ?: ""
                password = backStackEntry.arguments?.getString("password") ?: ""
            }
            ProfileScreen(navController)
        }
    }
}