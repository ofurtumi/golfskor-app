package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiRound(
    val id: Int,
    val courseName: String,
    val username: String,
    val holes: List<Int>,
    val score: Int
)