package hugbo.golfskor.entities

class Course (
        private val courseId: Int,
        private val courseName: String,
        private val coursePar: List<Int>,
        private var rounds: List<Round>
    ) {
    fun getCourseId(): Int {
        return courseId
    }

    fun getCourseName(): String {
        return courseName
    }

    fun getCoursePar(): List<Int> {
        return coursePar
    }

    fun getRounds(): List<Round> {
        return rounds
    }

    fun addRound(newRound: Round) {
        rounds += newRound
    }
}