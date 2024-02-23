package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch
import okio.IOException

sealed interface CourseUiState {
    data object Loading : CourseUiState
    data class Success(
        val courses: List<ApiCourse>,
        val username: String,
        val userId: Int,
        val authToken: String
    ) :
        CourseUiState

    data class Error(val message: String) : CourseUiState
}

class CourseViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val username = savedStateHandle.get<String>("username") ?: ""
    val userId = savedStateHandle.get<Int>("userId") ?: -1
    val authToken = savedStateHandle.get<String>("authToken") ?: ""
    var courseUiState: CourseUiState by mutableStateOf(CourseUiState.Loading)
        private set

    init {
        getGolfCourses()
        Log.d("CourseViewModel", "Username: $username, userId: $userId, authToken: $authToken")
    }

    fun refresh() {
        courseUiState = CourseUiState.Loading

        viewModelScope.launch {
            courseUiState = try {
                val listResult = GolfSkorApi.retrofitService.getCourses()
                CourseUiState.Success(listResult, username, userId, authToken)
            } catch (e: IOException) {
                CourseUiState.Error("Error: ${e.message}")
            }
        }
    }

    private fun getGolfCourses() {
        viewModelScope.launch {
            courseUiState = try {
                val listResult = GolfSkorApi.retrofitService.getCourses()
                CourseUiState.Success(listResult, username, userId, authToken)
            } catch (e: IOException) {
                CourseUiState.Error("Error: ${e.message}")
            }
        }
    }
}