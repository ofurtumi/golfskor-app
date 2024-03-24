package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.calculateHandicap
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch
import okio.IOException

sealed interface CourseUiState {
    data object Loading : CourseUiState
    data class Success(
        val courses: List<ApiCourse>,
        val handicap: Double = 0.0
    ) :
        CourseUiState

    data class Error(val message: String) : CourseUiState
}

class CourseViewModel : ViewModel() {
    var courseUiState: CourseUiState by mutableStateOf(CourseUiState.Loading)
        private set

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

    fun getGolfCourses(username: String, authToken: String) {
        viewModelScope.launch {
            val userInfo = GolfSkorApi.retrofitService.getUserRounds(username, "Bearer $authToken")
            val handicap = calculateHandicap(userInfo.rounds)
            courseUiState = try {
                val listResult = GolfSkorApi.retrofitService.getCourses()
                CourseUiState.Success(listResult, handicap)
            } catch (e: IOException) {
                CourseUiState.Error("Error: ${e.message}")
            }
        }
    }
}