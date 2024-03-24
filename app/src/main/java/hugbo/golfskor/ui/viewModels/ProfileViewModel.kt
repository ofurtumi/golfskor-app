package hugbo.golfskor.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hugbo.golfskor.calculateHandicap
import hugbo.golfskor.data.UserInfoDataStoreService
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

    fun getProfileRounds(username: String, authToken: String) {
        viewModelScope.launch {
            profileUiState = try {
                Log.d("Authorization", "Bearer $authToken")
                val locInfo = GPSLocation()
                val userInfoResult =
                    GolfSkorApi.retrofitService.getUserRounds(username, "Bearer $authToken")
                ProfileUiState.Success(
                    userInfoResult.rounds,
                    calculateHandicap(userInfoResult.rounds),
                    username,
                    userInfoResult.id,
                    authToken,
                    locInfo.getLatitude(),
                    locInfo.getLongitude(),
                    locInfo.getDirection()
                )

            } catch (e: Exception) {
                ProfileUiState.Error("Error: ${e.message}")
            }
        }

    }

    fun signOut() {
        viewModelScope.launch {
            UserInfoDataStoreService.clearUserInfo()
            profileUiState = ProfileUiState.SignedOut
        }
    }

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
    fun getWeather(){
        val gpsLocation = GPSLocation()
        //ProfileUiState.Weather.heat =  gpsLocation.getLatitude()
        //ProfileUiState.Weather.wind = gpsLocation.getLatitude()
    }
}