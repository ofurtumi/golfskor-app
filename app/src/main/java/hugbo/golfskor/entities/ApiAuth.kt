package hugbo.golfskor.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAuth(
    val id: Int,
    val username: String,
    @SerialName("accessToken")
    val authToken: String,
    val tokenType: String
)