package hugbo.golfskor.entities

class User (
    private var id: Int = 0,
    private var username: String = "Test User",
    private var authToken: String = "blaksdfjlaskdfjlaskdfjlaskdfjalskdfjlaskdfjlkasdjlkj",
    ) {
    private var rounds: List<Round> = listOf()
    fun getId(): Int {
        return id
    }

    fun setId(newId: Int) {
        id = newId
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(newUsername: String) {
        username = newUsername
    }

    fun getToken(): String {
        return authToken
    }

    fun setToken(newToken: String) {
        authToken = newToken
    }

    fun getRounds(): List<Round> {
        return rounds
    }

    fun addRound(newRound: Round) {
        rounds += newRound
    }

    override fun toString(): String {
        val roundString = rounds.joinToString("\n")
        return "User(id=$id, username='$username', authToken='$authToken', rounds='$roundString'"
    }
}