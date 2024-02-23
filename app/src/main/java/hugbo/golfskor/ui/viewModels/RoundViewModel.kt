package hugbo.golfskor.ui.viewModels

import android.util.Log
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
    data class Loading(val username: String, val userId: Int, val authToken: String) : RoundUiState
    data class NewRound(val holes: List<Int>, val username: String, val authToken: String) :
        RoundUiState

    data class OldRound(val round: ApiRound, val username: String, val authToken: String) :
        RoundUiState

    data class Error(val message: String) : RoundUiState
}

class RoundViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val roundType = savedStateHandle.get<String>("type") ?: "new"
    val username = savedStateHandle.get<String>("username") ?: ""
    val userId = savedStateHandle.get<Int>("userId") ?: -1
    val authToken = savedStateHandle.get<String>("authToken") ?: ""
    val id = savedStateHandle.get<Int>("id") ?: -1

    var roundUiState: RoundUiState by mutableStateOf(
        RoundUiState.Loading(
            username,
            userId,
            authToken
        )
    )
        private set

    init {
        getRoundOfType(username, authToken, roundType, id)
    }

    private fun getRoundOfType(
        username: String,
        authToken: String,
        roundType: String,
        roundId: Int
    ) {
        viewModelScope.launch {
            roundUiState = try {
                if (roundType == "new") {
                    RoundUiState.NewRound(listOf(1, 1, 1, 1, 1, 1, 1, 1, 1), username, authToken)
                } else {
                    val oldRound =
                        GolfSkorApi.retrofitService.getRound(roundId)
                    RoundUiState.OldRound(oldRound, username, authToken)
                }
            } catch (e: Exception) {
                RoundUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun postRound(holes: List<Int>) {
        viewModelScope.launch {
            roundUiState = try {
                Log.d("Holes", holes.joinToString())
                val round = GolfSkorApi.retrofitService.postRound(
                    id,
                    holes,
                    userId,
                    "Bearer $authToken"
                )
                RoundUiState.OldRound(round, username, authToken)
            } catch (e: Exception) {
                RoundUiState.Error("Error: ${e.message}")
            }
        }
    }
}