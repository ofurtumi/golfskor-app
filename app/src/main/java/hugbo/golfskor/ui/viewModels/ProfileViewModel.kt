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

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data object Deleting : ProfileUiState
    data class Success(
        val rounds: List<ApiRound>,
        val handicap: Double,
        val username: String,
        val userId: Int,
        val authToken: String
    ) : ProfileUiState

    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val userId = savedStateHandle.get<Int>("userId") ?: -1;
    private val username: String = savedStateHandle.get<String>("username") ?: ""
    private val authToken: String = savedStateHandle.get<String>("authToken") ?: ""

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        //getProfileRounds(username, authToken)
    }

    fun getProfileRounds(username: String, authToken: String) {
        viewModelScope.launch {
            profileUiState = try {
                Log.d("Authorization", "Bearer $authToken")
                val userInfoResult =
                    GolfSkorApi.retrofitService.getUserRounds(username, "Bearer $authToken")
                ProfileUiState.Success(
                    userInfoResult.rounds,
                    calculateHandicap(userInfoResult.rounds),
                    username,
                    userInfoResult.id,
                    authToken
                )
            } catch (e: Exception) {
                ProfileUiState.Error("Error: ${e.message}")
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

    fun deleteRound(roundId: Int) {
        Log.d("Deleting by roundId",  "$roundId")
        viewModelScope.launch {

            profileUiState = try {
                Log.d("Deleting by roundId",  "$roundId")
                println("asdf")
                 val result = GolfSkorApi.retrofitService.deleteRound(
                     "Bearer $authToken",
                     roundId,
                     userId,
                 )
                // TODO eyda round ur oldstate og skila
                ProfileUiState.Deleting


            } catch (e: Exception) {
                Log.e("Error deleting round", "$roundId", e)
                ProfileUiState.Error("Villa við að eyða :'(")
            }
        }
    }
}