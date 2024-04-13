package hugbo.golfskor

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import hugbo.golfskor.ui.viewModels.NavViewModel

/**
 * Sealed class representing different screens in the application.
 *
 * This sealed class defines specific screens available in the application, encapsulating the routing information,
 * the title for display purposes, and an icon associated with each screen.
 *
 * The class has different data objects for each screen, each providing a unique route, title, and icon:
 * - {@code Courses}: Represents the courses screen with route 'Courses', title 'Vellir', and an icon indicating a location.
 * - {@code Profile}: Represents the profile screen with route 'Profile', title 'Prófíll', and an icon for user account.
 */
sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    data object Courses : Screens("Courses", "Vellir", Icons.Filled.LocationOn)
    data object Profile :
        Screens("Profile", "Prófíll", Icons.Filled.AccountCircle)
}

/**
 * Composable function that sets up and manages the navigation for the application.
 *
 * This function creates a navigation controller and defines the navigation graph for the application. It uses a `Scaffold`
 * to provide a consistent structure across screens, including a bottom navigation bar. The bottom navigation bar is
 * populated with icons and labels defined in the `Screens` enum, facilitating navigation between different sections
 * of the app such as Courses and Profile.
 *
 * The navigation setup excludes the bottom bar on the 'Authenticate' route to provide a full-screen experience for
 * login or registration processes. Each screen is defined as a composable within the `NavHost`, which handles the
 * transitions and state management during navigation.
 *
 * This function also handles the navigation logic, ensuring that navigation events respect the application's overall
 * state, such as maintaining the state or resetting navigation when navigating to the top-level destinations.
 *
 * @param navViewModel ViewModel that handles the navigation state and data for the application. Defaults to a new instance
 *                     remembered across recompositions if not provided.
 */
@Composable
fun Nav(
    navViewModel: NavViewModel = remember { NavViewModel() }
) {
    val navController = rememberNavController()

    val screens: List<Screens> = listOf(
        Screens.Courses,
        Screens.Profile,
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (currentDestination?.route == "Authenticate") {
                return@Scaffold
            }

            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary) {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = screen.title,
                                tint = MaterialTheme.colorScheme.background
                            )
                        },
                        label = {
                            Text(screen.title, color = MaterialTheme.colorScheme.background)
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        },

                        )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "Authenticate") {
            composable(route = "Rounds/{type}/{id}/{courseName}",
                arguments = listOf(
                    navArgument(name = "type") {
                        type = NavType.StringType
                    },
                    navArgument(name = "id") {
                        type = NavType.IntType
                    },
                    navArgument(name = "courseName") {
                        type = NavType.StringType
                    }
                )) { RoundScreen(innerPadding, navController, navViewModel) }
            composable(route = "Authenticate") {
                AuthenticateScreen(
                    innerPadding,
                    navController,
                    navViewModel
                )
            }
            composable(route = "Courses") {
                CoursesScreen(
                    innerPadding,
                    navController,
                    navViewModel
                )
            }
            composable(route = "Profile") {
                ProfileScreen(
                    innerPadding,
                    navController,
                    navViewModel
                )
            }
        }
    }
}