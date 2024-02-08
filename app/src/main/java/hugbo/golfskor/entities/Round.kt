package hugbo.golfskor.entities

class Round(private val id: Int = 0, private val courseName: String = "Test Course", private val username: String = "Tester") {
    private var holes: List<Int> = listOf()

    fun create(newHoles: List<Int> ): List<Int> {
        holes = newHoles
        return holes
    }

    fun update(newHoles: List<Int> ): List<Int> {
        if (newHoles.size != holes.size) {
            throw IllegalArgumentException("New holes list must be the same size as the old one")
        }
        holes = newHoles
        return holes
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
}