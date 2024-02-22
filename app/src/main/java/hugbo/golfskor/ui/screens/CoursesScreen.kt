package hugbo.golfskor.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.ui.GolfCourseList
import hugbo.golfskor.ui.viewModels.CourseUiState
import hugbo.golfskor.ui.viewModels.CourseViewModel

@Composable
fun CoursesScreen(
    courseViewModel: CourseViewModel = viewModel()
) {
    val courseUiState = courseViewModel.courseUiState
    Column {
        Button(onClick = { courseViewModel.refresh() }) {
            Text(text = "Sækja aftur")
        }
        when (courseUiState) {
            is CourseUiState.Loading -> LoadingScreen()
            is CourseUiState.Success -> ResultScreen(courseUiState.courses)
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
fun ResultScreen(courses: List<ApiCourse>) {
    GolfCourseList(courses)
}