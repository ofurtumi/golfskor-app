package hugbo.golfskor

import hugbo.golfskor.entities.ApiRound

fun calculateHandicap(rounds: List<ApiRound>): Double {
    val scores = rounds.map {
        if (it.holes.size <= 9) {
            it.score * 2
        } else {
            it.score
        }
    }
    var average = scores.sorted()
    if (average.size > 20) {
        average = scores.subList(0, 20).sorted()
    }
    if (average.size > 8) {
        average = average.subList(0, 9)
    }
    var score = 126.0
    if (average.isNotEmpty()) {
        score = (average.sum().toFloat() / average.size.toFloat()).toDouble()
    }
    return score - 72.0
}