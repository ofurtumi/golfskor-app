package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiLocation(
    val date: String,
    val windspeed: Double,
    val direction: String,
    val temperature: Double

)
