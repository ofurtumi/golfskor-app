package hugbo.golfskor

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hugbo.golfskor.ui.screens.AuthenticateScreen
import hugbo.golfskor.ui.screens.CoursesScreen
import hugbo.golfskor.ui.screens.ProfileScreen
import hugbo.golfskor.ui.screens.RoundScreen

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    data object Courses : Screens("Courses", "Vellir", Icons.Filled.LocationOn)
    data object Profile :
        Screens("Profile/{username}/{password}", "Prófíll", Icons.Filled.AccountCircle)

    data object Rounds : Screens("Rounds", "Rounds", Icons.Filled.Add)
}

@Composable
fun Nav() {
    val navController = rememberNavController()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val screens: List<Screens> = listOf(
        Screens.Courses,
        Screens.Profile,
        Screens.Rounds
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (currentDestination?.route == "Authenticate") {
                return@Scaffold
            }

            BottomNavigation {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        modifier = Modifier.background(color = MaterialTheme.colors.primary),
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { screen.title },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "Authenticate") {
            composable(route = "Rounds") {
                RoundScreen()
            }
            composable(route = "Authenticate") {
                AuthenticateScreen(navController = navController)
            }
            composable(route = "Courses") {
                CoursesScreen()
            }
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
            ) { backStackEntry ->
                if (username == "" && password == "") {
                    username = backStackEntry.arguments?.getString("username") ?: ""
                    password = backStackEntry.arguments?.getString("password") ?: ""
                }
                ProfileScreen(innerPadding, navController = navController)
            }
        }
    }
}