package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiCourse(
    val id: Int,
    val courseName: String,
    val coursePars: List<Int>,
    val rounds: List<ApiRound>
)