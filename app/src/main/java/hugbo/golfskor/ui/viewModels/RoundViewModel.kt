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