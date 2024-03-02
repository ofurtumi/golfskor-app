package hugbo.golfskor.entities

/**
 * This class is used to preview the data that is used in the app
 */
fun previewRound(): ApiRound {
    return ApiRound(
        1,
        "Test Course",
        "Tester",
        listOf(1, 2, 3, 1, 2, 3, 1, 2, 3),
        18
    )
}

fun previewCourse(): ApiCourse {
    return ApiCourse(
        1,
        "Test Course",
        List(9) { 1 },
        List(3) { previewRound() }
    )
}