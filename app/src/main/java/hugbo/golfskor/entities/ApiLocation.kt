package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiLocation(
    val Latitude: Double,
    val Longitude: Double

)
