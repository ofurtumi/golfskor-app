package hugbo.golfskor.entities

import kotlinx.serialization.Serializable

@Serializable
data class ApiLocation(
    val date: String,
    val wind: Double,
    val direction: String,
    val heat: Double

)
