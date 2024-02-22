package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.entities.ApiUserInfo
import hugbo.golfskor.network.GolfSkorApi
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(
        val userInfo: ApiUserInfo,
        val handicap: Double,
        val authToken: String
    ) : ProfileUiState

    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val username: String = savedStateHandle.get<String>("username") ?: ""
    private val authToken: String = savedStateHandle.get<String>("password") ?: ""

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        getProfileRounds(username, authToken)
    }

    fun getProfileRounds(username: String, authToken: String) {
        viewModelScope.launch {
            profileUiState = try {
                Log.d("Autherization", "Bearer $authToken")
                val userInfoResult =
                    GolfSkorApi.retrofitService.getUserRounds(username, "Bearer $authToken")
                ProfileUiState.Success(
                    userInfoResult,
                    calculateHandicap(userInfoResult.rounds),
                    authToken
                )
            } catch (e: Exception) {
                ProfileUiState.Error("Error: ${e.message}")
            }
        }
    }

    // fun addRound() {
    //     var id = 1
    //     if (_uiState.value.rounds.isNotEmpty()) {
    //         id = _uiState.value.rounds.last().getId() + 1
    //     }
    //     val newRound = Round(
    //         id = id,
    //         courseName = "Test Course 2",
    //         username = uiState.value.username,
    //         holes = listOf(3, 4, 5, 4, 3, 4, 5, 4, 3)
    //     )

    //     Log.d("addRound", "$newRound")

    //     val newRounds = uiState.value.rounds.toMutableList()
    //     newRounds.add(newRound)

    //     _uiState.update {
    //         it.copy(
    //             rounds = newRounds,
    //             handicap = calculateHandicap(newRounds),
    //             totalScore = newRounds.sumOf { round -> round.getScore() }
    //         )
    //     }
    // }

    // fun editRound(id: Int) {
    //     val newRounds = uiState.value.rounds.toMutableList()

    //     newRounds.map { round ->
    //         if (round.getId() == id) {
    //             round.update(round.getHoles().map { it + 1 })
    //         }
    //     }

    //     _uiState.update {
    //         it.copy(
    //             rounds = newRounds,
    //             handicap = calculateHandicap(newRounds),
    //             totalScore = newRounds.sumOf { round -> round.getScore() },
    //         )
    //     }
    // }

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
            average = scores.reversed().subList(0, 20).sorted()
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

    // fun deleteRound(id: Int) {
    //     // Should call the api
    //     // Todo: Delete round from api
    //     val newRounds = uiState.value.rounds.toMutableList()

    //     newRounds.removeIf { it.getId() == id }

    //     _uiState.update {
    //         it.copy(
    //             rounds = newRounds,
    //             handicap = calculateHandicap(newRounds),
    //             totalScore = newRounds.sumOf { round -> round.getScore() },
    //         )
    //     }
    // }
}