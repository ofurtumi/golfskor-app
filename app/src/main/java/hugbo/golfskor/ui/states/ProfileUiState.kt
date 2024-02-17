package hugbo.golfskor.ui.states

import hugbo.golfskor.entities.Round

data class ProfileUiState (
    val id: Int = 0,
    val username: String = "Test User",
    val authToken: String = "blaksdfjlaskdfjlaskdfjlaskdfjalskdfjlaskdfjlkasdjlkj",
    val rounds: List<Round> = listOf(),
    val handicap: Int = 0
)