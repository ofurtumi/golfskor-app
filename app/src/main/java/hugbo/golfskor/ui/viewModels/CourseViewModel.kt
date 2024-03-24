package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun calculateHandicap(rounds: List<ApiRound>): Double {
        val scores = rounds.map {
            if (it.holes.size <= 9) {
                it.score * 2
            } else {
                it.score
            }
        }
        var average = scores.sorted()
        if (average.size > 20) {
            average = scores.subList(0, 20).sorted()
        }
        if (average.size > 8) {
            average = average.subList(0, 9)
        }
        var score = 126.0
        if (average.isNotEmpty()) {
            score = (average.sum().toFloat() / average.size.toFloat()).toDouble()
        }
        return score - 72.0
    }

    fun getGolfCourses(username: String, userId: Int, authToken: String) {
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