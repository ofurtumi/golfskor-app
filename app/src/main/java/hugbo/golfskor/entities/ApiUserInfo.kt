package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiUserInfo(
    val id: Int,
    val username: String,
    val rounds: List<ApiRound>,
)