package hugbo.golfskor.entities

class Round(
    private var id: Int = 0,
    private val courseName: String = "Test Course",
    private val username: String = "Tester",
    private var holes: List<Int> = listOf(3,4,3,4,3,4,3,4,3)
) {
    fun update(newHoles: List<Int> ): List<Int> {
        if (newHoles.size != holes.size) {
            throw IllegalArgumentException("New holes list must be the same size as the old one")
        }
        holes = newHoles
        return holes
    }

    fun setId(newId: Int) {
        id = newId
    }

    fun getId(): Int {
        return id
    }

    fun delete(): Boolean {
        holes = listOf()
        return true
    }

    fun getScore(): Int {
        return holes.sum()
    }

    fun getUsername(): String {
        return username
    }

    fun getCourseName(): String {
        return courseName
    }

    fun getHoles(): List<Int> {
        return holes
    }

    fun getHoleString(): String {
        return holes.joinToString()
    }

    // ToString method for debugging
    override fun toString(): String {
        return "Round(id=$id, courseName='$courseName', username='$username', holes=$holes)"
    }
}