package hugbo.golfskor.entities

import kotlin.math.abs
import kotlin.random.Random

/**
 * This class is used to preview the data that is used in the app
 */
fun previewRound(big: Boolean = false): ApiRound {
    val holes = List(if (big) 18 else 9) { abs(Random.nextInt() % 4) + 1 }
    return ApiRound(
        1,
        "Test Course",
        "Tester",
        holes,
        holes.sum()
    )
}

fun previewCourse(): ApiCourse {
    return ApiCourse(
        1,
        "Test Course",
        List(9) { 1 },
        listOf(previewRound(), previewRound(true), previewRound())
    )
}