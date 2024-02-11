package hugbo.golfskor

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hugbo.golfskor.ui.screens.AuthenticateScreen
import hugbo.golfskor.ui.screens.CoursesScreen
import hugbo.golfskor.ui.screens.ProfileScreen

@Composable
fun Nav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Authenticate") {
        composable(route = "Authenticate") {
            AuthenticateScreen(navController)
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
            ProfileScreen(
                username = backStackEntry.arguments?.getString("username"),
                password = backStackEntry.arguments?.getString("password")
            )
        }
    }
}