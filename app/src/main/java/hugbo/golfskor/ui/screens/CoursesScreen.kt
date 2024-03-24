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

        Button(onClick = { courseViewModel.refresh() }) {
            Text(text = "Sækja aftur")
        }
        when (courseUiState) {
            is CourseUiState.Loading -> {
                LoadingScreen(stringResource(R.string.fetching_courses))
                courseViewModel.getGolfCourses(
                    navViewModel.navUiState.username,
                    navViewModel.navUiState.userId,
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