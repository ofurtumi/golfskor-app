package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch
import okio.IOException

sealed interface CourseUiState {
    object Loading : CourseUiState
    data class Success(val courses: List<ApiCourse>) : CourseUiState
    data class Error(val message: String) : CourseUiState
}

class CourseViewModel : ViewModel() {
    var courseUiState: CourseUiState by mutableStateOf(CourseUiState.Loading)
        private set

    init {
        getGolfCourses()
    }

    fun refresh() {
        courseUiState = CourseUiState.Loading

        viewModelScope.launch {
            courseUiState = try {
                val listResult = GolfSkorApi.retrofitService.getCourses()
                CourseUiState.Success(listResult)
            } catch (e: IOException) {
                CourseUiState.Error("Error: ${e.message}")
            }
        }
    }

    private fun getGolfCourses() {
        viewModelScope.launch {
            courseUiState = try {
                val listResult = GolfSkorApi.retrofitService.getCourses()
                CourseUiState.Success(listResult)
            } catch (e: IOException) {
                CourseUiState.Error("Error: ${e.message}")
            }
        }
    }
}