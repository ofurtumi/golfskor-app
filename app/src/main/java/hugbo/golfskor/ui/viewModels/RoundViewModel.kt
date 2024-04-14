package hugbo.golfskor.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch

sealed interface RoundUiState {
    data class Loading(
        val roundType: String,
        val roundId: Int
    ) : RoundUiState

    data class NewRound(
        val holes: List<Int>,
        val username: String,
        val authToken: String,
        val buttonText: String,
        val courseName: String
    ) : RoundUiState

    data class OldRound(
        val round: ApiRound,
        val username: String,
        val authToken: String,
        val buttonText: String,
        val courseName: String
    ) : RoundUiState

    data class Success(
        val message: String
    ) : RoundUiState

    data class Error(
        val message: String
    ) : RoundUiState
}

class RoundViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val roundType = savedStateHandle.get<String>("type") ?: "new"
    val id = savedStateHandle.get<Int>("id") ?: -1
    val courseName = savedStateHandle.get<String>("courseName") ?: "Nýr hringur"

    var roundUiState: RoundUiState by mutableStateOf(
        RoundUiState.Loading(
            roundType,
            id
        )
    )
        private set

    /**
     * Fetches or initializes a golf round based on the specified type and updates the round UI state accordingly.
     *
     * This function is responsible for setting up the appropriate UI state for different types of golf rounds.
     * It uses a coroutine within the {@code viewModelScope} to handle asynchronous operations, ensuring the process
     * is lifecycle-aware and efficiently managed. Based on the round type specified, the function performs one of the
     * following actions:
     *
     * - "new": Initializes a new round with a predefined number of holes (9 or 18), setting the state to {@code RoundUiState.NewRound}.
     * - "big": Similar to "new", but specifically initializes a full 18-hole round.
     * - Default: Fetches an existing round from the server using its ID, setting the state to {@code RoundUiState.OldRound}.
     *
     * If any operation fails during execution, such as due to network errors or data issues, the function catches the
     * exception and updates the state to {@code RoundUiState.Error} with a relevant error message.
     *
     * @param username The username of the user associated with the round. Used for authentication in API calls.
     * @param authToken The authentication token to validate the user's session during the API call.
     * @param roundType The type of the round, which dictates whether a new round is created or an existing one is fetched.
     * @param roundId The unique identifier of the round, used when fetching details of an existing round.
     */
    fun getRoundOfType(
        username: String,
        authToken: String,
        roundType: String,
        roundId: Int
    ) {
        viewModelScope.launch {
            roundUiState = try {
                when (roundType) {
                    "new" -> {
                        RoundUiState.NewRound(
                            List(9) { 1 },
                            username,
                            authToken,
                            "Skrá hring",
                            courseName
                        )
                    }

                    "big" -> {
                        RoundUiState.NewRound(
                            List(18) { 1 },
                            username,
                            authToken,
                            "Skrá hring",
                            courseName
                        )
                    }

                    else -> {
                        val oldRound =
                            GolfSkorApi.retrofitService.getRound(roundId)
                        RoundUiState.OldRound(
                            oldRound,
                            username,
                            authToken,
                            "Uppfæra hring",
                            courseName
                        )
                    }
                }
            } catch (e: Exception) {
                RoundUiState.Error("Error: ${e.message}")
            }
        }
    }

    /**
     * Updates the details of an existing golf round based on user inputs and API interactions.
     *
     * This function is responsible for updating an existing round with new hole scores. It performs this action
     * asynchronously within the ViewModel's {@code viewModelScope}
     * The function sets the {@code roundUiState} initially to indicate an ongoing operation and
     * attempts to update the round through an API call.
     *
     * The steps performed by the function are:
     * - Sending an API request to update the round details on the server using the provided round ID and new hole scores.
     * - Upon successful update, the UI state is set to {@code RoundUiState.Success} with a success message indicating
     *   that the round was successfully updated.
     * - If the API call fails for any reason, such as a network error or server-side failure, the UI state is set to
     *   {@code RoundUiState.Error} with an appropriate error message detailing the issue.
     *
     * @param holes A list of integers representing the updated scores for each hole in the round.
     * @param roundId The unique identifier of the round being updated.
     * @param userId The identifier of the user associated with the round, used for validation and authorization in the API call.
     * @param authToken The authentication token to validate the user's session and authorize the update operation.
     */
    fun updateRound(holes: List<Int>, roundId: Int, userId: Int, authToken: String) {
        viewModelScope.launch {
            roundUiState = try {
                GolfSkorApi.retrofitService.updateRound(
                    roundId,
                    holes,
                    userId,
                    "Bearer $authToken"
                )
                RoundUiState.Success("Tókst að breyta hring")
            } catch (e: Exception) {
                RoundUiState.Error("Error: ${e.message}")
            }
        }
    }

    /**
     * Submits a new golf round to the server.
     *
     * This function is responsible for creating a new round with specified hole scores for a user. It performs this action
     * asynchronously within the ViewModel's {@code viewModelScope} to ensure that it is lifecycle-aware and avoids memory leaks.
     * The function sets the {@code roundUiState} initially to indicate an ongoing operation and attempts to post the new round
     * through an API call.
     *
     * The steps performed by the function are:
     * - Sending an API request to create a new round on the server using the provided hole scores and user ID.
     * - Upon successful creation, the UI state is set to {@code RoundUiState.Success} with a message indicating
     *   that the round was successfully recorded.
     * - If the API call fails for any reason, such as a network error or server-side failure, the UI state is set to
     *   {@code RoundUiState.Error} with an appropriate error message detailing the issue.
     *
     * @param holes A list of integers representing the scores for each hole in the new round.
     * @param userId The identifier of the user for whom the round is being recorded, used for validation and authorization in the API call.
     * @param authToken The authentication token to validate the user's session and authorize the operation.
     */
    fun postRound(holes: List<Int>, userId: Int, authToken: String) {
        viewModelScope.launch {
            roundUiState = try {
                GolfSkorApi.retrofitService.postRound(
                    id,
                    holes,
                    userId,
                    "Bearer $authToken"
                )
                RoundUiState.Success("Tókst að skrá hring")
            } catch (e: Exception) {
                RoundUiState.Error("Error: ${e.message}")
            }
        }
    }
}