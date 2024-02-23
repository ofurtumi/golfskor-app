package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hugbo.golfskor.ui.GolfCourseList
import hugbo.golfskor.ui.viewModels.CourseUiState
import hugbo.golfskor.ui.viewModels.CourseViewModel
import hugbo.golfskor.ui.viewModels.NavUiState
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
            is CourseUiState.Loading -> LoadingScreen()
            is CourseUiState.Success -> ResultScreen(
                courseUiState,
                navController,
                navViewModel.navUiState
            )

            is CourseUiState.Error -> ErrorScreen(courseUiState.message)
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
}

@Composable
fun LoadingScreen() {
    Text(text = "Loading")
}

@Composable
fun ResultScreen(
    state: CourseUiState.Success,
    navController: NavController,
    navUiState: NavUiState
) {
    GolfCourseList(state, navController, navUiState)
}