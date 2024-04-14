package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hugbo.golfskor.R
import hugbo.golfskor.ui.ErrorScreen
import hugbo.golfskor.ui.GolfCourseList
import hugbo.golfskor.ui.LoadingScreen
import hugbo.golfskor.ui.viewModels.CourseUiState
import hugbo.golfskor.ui.viewModels.CourseViewModel
import hugbo.golfskor.ui.viewModels.NavViewModel
/**
 * Composable function that displays the main screen for viewing golf courses.
 *
 * This screen shows different UI states based on the data loading and fetching process from a remote server.
 * It uses the {@code courseViewModel} to manage and observe course data states such as loading, success, and error.
 * Depending on the state of {@code courseUiState}, this screen may display a loading animation, a list of golf courses,
 * or an error message. The screen's layout and behavior are dynamically adjusted based on these states.
 *
 * - When in the 'Loading' state, it triggers data fetching through {@code courseViewModel} and shows a loading screen.
 * - In the 'Success' state, it displays a list of golf courses, which users can interact with.
 * - The 'Error' state shows an appropriate error message.
 *
 * The function also manages padding and alignment to ensure the UI is well-organized and visually appealing.
 *
 * @param innerPadding Padding to apply to the content inside the screen.
 * @param navController Controller for navigating between screens.
 * @param navViewModel ViewModel that holds navigation and user session data.
 * @param courseViewModel ViewModel for managing golf course data. It provides and updates the course UI state.
 */
@Composable
fun CoursesScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    navViewModel: NavViewModel,
    courseViewModel: CourseViewModel = viewModel(),
) {
    val courseUiState = courseViewModel.courseUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        when (courseUiState) {
            is CourseUiState.Loading -> {
                LoadingScreen(stringResource(R.string.fetching_courses))
                courseViewModel.getGolfCourses(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.authToken
                )
            }

            is CourseUiState.Success -> GolfCourseList(
                courseUiState,
                navController,
                courseUiState.handicap
            )

            is CourseUiState.Error -> ErrorScreen(courseUiState.message)
        }
    }
}