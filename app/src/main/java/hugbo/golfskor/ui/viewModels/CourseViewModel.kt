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

    /**
     * Fetches the list of golf courses and calculates the handicap based on user's previous rounds.
     *
     * This function is triggered to retrieve detailed information about golf courses available on the platform
     * and the user's handicap, which is computed from the rounds they have played. The function performs several operations:
     *
     * 1. Initiates a coroutine within {@code viewModelScope} to ensure that the operation is managed within the lifecycle
     *    of the ViewMode.
     * 2. Fetches user rounds using the API, providing authentication via username and bearer token.
     * 3. Calculates the user's handicap based on these rounds using the {@code calculateHandicap} function.
     * 4. Attempts to fetch a list of golf courses from the server. On successful retrieval, updates the UI state to
     *    {@code CourseUiState.Success} with the list of courses and the calculated handicap.
     * 5. Catches any IOExceptions that occur during the API calls and updates the UI state to {@code CourseUiState.Error}
     *    with an appropriate error message.
     *
     * This method ensures that the UI state is always updated based on the outcome of the API calls, providing feedback
     * to the user about the status of their request.
     *
     * @param username The username of the user whose rounds are to be fetched.
     * @param authToken The authentication token used to validate the request.
     */
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