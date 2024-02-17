package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import hugbo.golfskor.entities.Round
import hugbo.golfskor.entities.User
import hugbo.golfskor.ui.states.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
   private val _uiState = MutableStateFlow(ProfileUiState())
   val uiState: StateFlow<ProfileUiState> = _uiState

    private val username: String = savedStateHandle.get<String>("username") ?: ""
    private val password: String = savedStateHandle.get<String>("password") ?: ""
    // this user should be gotten from api
    // TODO: Fetch user from api
    private val user: User = User(username = username, authToken = password)
    // private var rounds by mutableStateOf(mutableListOf<Round>())

    init {
        // user = getUserFromApi(username, authToken)
        _uiState.value = ProfileUiState(
            id = user.getId(),
            username = user.getUsername(),
            authToken = user.getToken(),
            rounds = user.getRounds(),
            handicap = 0
        )
    }

    fun addRound() {
        var id = 1
        if (_uiState.value.rounds.isNotEmpty()) {
            id = _uiState.value.rounds.last().getId() + 1
        }
        val newRound = Round(
            id = id,
            courseName = "Test Course 2",
            username = uiState.value.username,
            holes = listOf(3, 4, 5, 4, 3, 4, 5, 4, 3)
        )

        Log.d("addRound", "$newRound")

        val newRounds = uiState.value.rounds.toMutableList()
        newRounds.add(newRound)

        _uiState.update {
            it.copy(
                rounds = newRounds,
                handicap = calculateHandicap(newRounds),
            )
        }
    }

    fun editRound(id: Int) {
        val newRounds = uiState.value.rounds.toMutableList()

        newRounds.map {round ->
            if (round.getId() == id) {
                round.update(round.getHoles().map { it + 1 })
            }
        }

        _uiState.update {
            it.copy(
                handicap = calculateHandicap(newRounds),
                rounds = newRounds
            )
        }
    }

    private fun calculateHandicap(rounds: List<Round>): Int {
        val scores = rounds.map {
            if (it.getHoles().size > 9) {
                it.getScore() * 2
            } else {
                it.getScore()
            }
        }
        var average = scores.sorted()
        if (average.size > 20) {
            average = scores.reversed().subList(0, 20).sorted()
        }
        if (average.size > 8) {
            average = average.subList(0, 9)
        }
        Log.d("calculateHandicap", "$average")
        return average.sum() / average.size
    }

    fun deleteRound(id: Int) {
        // Should call the api
        // Todo: Delete round from api
        val newRounds = uiState.value.rounds.toMutableList()

        newRounds.removeIf { it.getId() == id }

        _uiState.update {
            it.copy(
                handicap = calculateHandicap(newRounds),
                rounds = newRounds
            )
        }
    }
}