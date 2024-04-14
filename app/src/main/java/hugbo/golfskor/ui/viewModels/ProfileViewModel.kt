package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.calculateHandicap
import hugbo.golfskor.data.UserInfoDataStoreService
import hugbo.golfskor.entities.ApiLocation
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.service.GPSLocation
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data object Deleting : ProfileUiState

    data class Success(
        val rounds: List<ApiRound>,
        val handicap: Double,
        val username: String,
        val userId: Int,
        val authToken: String,
        val heat: Double,
        val wind: Double,
        val direction: String
    ) : ProfileUiState

    data class RoundUpdateRequest(
        val courseId: Int,
        val holes: List<Int>,
        val userId: Int,
        val authToken: String,
    )

    data class Error(val message: String) : ProfileUiState
    data object SignedOut : ProfileUiState
}

class ProfileViewModel : ViewModel() {
    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    /**
     * Fetches the golf rounds for a specified user along with weather conditions and updates the profile UI state.
     *
     * This function is responsible for retrieving detailed information about a user's golf rounds and current weather conditions
     * based on the user's location. It performs this action asynchronously within the ViewModel's {@code viewModelScope} to ensure
     * that it is lifecycle-aware and avoids memory leaks. Upon initiation, the function logs the authorization process, fetches the
     * user's current location, retrieves the current weather from the weather service, and then fetches the user's golf rounds.
     *
     * The steps performed by the function are:
     * - Log the authorization token for debugging purposes.
     * - Retrieve the current GPS location coordinates.
     * - Calculate the city name based on the latitude and longitude.
     * - Fetch current weather data for the calculated city.
     * - Fetch the user's golf rounds from the server using the provided username and authentication token.
     * - Calculate the user's handicap based on the retrieved rounds.
     * - Update the profile UI state to {@code ProfileUiState.Success} with all retrieved and calculated data.
     * - If any operation fails (location fetch, API calls), the UI state is updated to {@code ProfileUiState.Error} with an appropriate
     *   error message detailing the issue.
     *
     * @param username The username of the user whose rounds are to be fetched.
     * @param authToken The authentication token to validate the user's session during the API calls.
     */
    fun getProfileRounds(username: String, authToken: String) {
        viewModelScope.launch {
            profileUiState = try {
                Log.d("Authorization", "Bearer $authToken")

                val locInfo = GPSLocation()
                val latitude = locInfo.getLatitude()
                val longitude = locInfo.getLongitude()
                val city = calculateCityFromCoordinates(latitude, longitude)
                val weatherResponse = GolfSkorApi.retrofitService.getWeather(authToken, city)

                val userInfoResult =
                    GolfSkorApi.retrofitService.getUserRounds(username, "Bearer $authToken")
                ProfileUiState.Success(
                    userInfoResult.rounds,
                    calculateHandicap(userInfoResult.rounds),
                    username,
                    userInfoResult.id,
                    authToken,
                    weatherResponse.windspeed,
                    weatherResponse.temperature,
                    locInfo.getDirection(weatherResponse.direction)
                )
            } catch (e: Exception) {
                ProfileUiState.Error("Error: ${e.message}")
            }
        }

    }

    /**
     * Logs out the current user by clearing stored user information and updating the profile UI state to reflect the logout.
     *
     * This function performs the logout operation asynchronously within the ViewModel's {@code viewModelScope} to ensure
     * it is managed within the ViewModel's lifecycle, preventing memory leaks. It involves clearing the user's stored
     * credentials from local storage and updating the UI state to indicate that the user has been signed out.
     *
     * The steps performed by the function are:
     * - Clear the user's stored information using {@code UserInfoDataStoreService}.
     * - Set the profile UI state to {@code ProfileUiState.SignedOut} to trigger UI updates across the application and
     *   redirect the user to the login screen or other initial views.
     *
     * This method effectively resets the user session and is typically called when the user initiates a logout action
     * from the UI.
     */
    fun signOut() {
        viewModelScope.launch {
            UserInfoDataStoreService.clearUserInfo()
            profileUiState = ProfileUiState.SignedOut
        }
    }

    /**
     * Deletes a specific golf round for a user based on the round's identifier.
     *
     * This function is responsible for the asynchronous deletion of a golf round from the server. It performs this operation
     * within the ViewModel's {@code viewModelScope} to ensure that it is lifecycle-aware and does not lead to memory leaks.
     * The function sets the profile UI state initially to indicate that a deletion operation is in progress and attempts
     * to delete the round through an API call.
     *
     * The steps performed by the function are:
     * - Sends an API request to delete a specific round using the provided round ID, user ID, and an authorization token.
     * - Upon successful deletion, the UI state is updated to {@code ProfileUiState.Deleting}, which triggers a UI update
     *   showing a message indicating that the round is being deleted.
     * - If the deletion attempt fails due to any exception (e.g., network issues, server-side failure, or permission errors),
     *   the UI state is updated to {@code ProfileUiState.Error} with an error message detailing the issue.
     *
     * @param roundId The unique identifier of the round to be deleted.
     * @param userId The identifier of the user associated with the round, used for validation and authorization in the API call.
     * @param authToken The authentication token to validate the user's session and authorize the deletion operation.
     */
    fun deleteRound(roundId: Int, userId: Int, authToken: String) {
        viewModelScope.launch {

            profileUiState = try {
                GolfSkorApi.retrofitService.deleteRound(
                    "Bearer $authToken",
                    roundId,
                    userId,
                )
                ProfileUiState.Deleting

            } catch (e: Exception) {
                ProfileUiState.Error("Villa við að eyða :'(")
            }
        }
    }

    /**
     * Calculates and returns a city name based on geographical latitude and longitude coordinates.
     *
     * This function is designed to deduce a city name from specific geographic coordinates, particularly focusing
     * on a method that subtracts reference values to determine if the coordinates fall within a particular region.
     * Currently, it uses hardcoded values to check if the coordinates correspond to the area near Reykjavík (denoted "rvk").
     *
     * The decision process involves:
     * - Subtracting a fixed latitude (64) and longitude (21) from the provided values.
     * - Converting the result to integers to simplify the comparison.
     * - Checking if both adjusted latitude and longitude are less than zero, which currently corresponds to the area
     *   around Reykjavík.
     *
     * If the conditions are met, it returns "rvk". If not, it returns an empty string, indicating no specific city
     * was matched.
     *
     * @param lat The latitude of the location to evaluate.
     * @param lon The longitude of the location to evaluate.
     * @return A string representing the city name ("rvk" for Reykjavík) or an empty string if no match is found.
     */
    private fun calculateCityFromCoordinates(lat: Double, lon: Double): String {
        val latInt = (lat - 64).toInt()
        val lonInt = (lon - 21).toInt()
        return if (latInt < 0 && lonInt < 0) "rvk" else ""
    }
}