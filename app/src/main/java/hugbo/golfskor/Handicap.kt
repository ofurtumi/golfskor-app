package hugbo.golfskor

import hugbo.golfskor.entities.ApiRound

/**
 * Calculates the golf handicap based on a list of provided golf rounds.
 *
 * This function computes the handicap using the average of the best scores, adjusted for rounds where
 * only nine holes were played. For nine-hole rounds, the score is doubled to estimate an 18-hole score.
 * The method involves several steps:
 *
 * 1. Adjust scores: Scores are doubled for nine-hole rounds to approximate an 18-hole score.
 * 2. Sorting and limiting: Scores are sorted in ascending order. If more than 20 scores are present,
 *    only the lowest 20 are considered. From these, only the lowest 9 scores are used for further calculations.
 * 3. Average calculation: The average of these scores is calculated.
 * 4. Handicap calculation: The final handicap is determined by subtracting the base score (typically 72 for a standard
 *    golf course) from the average score.
 *
 * The result reflects the player's potential ability relative to a par score, with a lower handicap indicating
 * a better golfer.
 *
 * @param rounds A list of {@code ApiRound} objects, each representing the scores from a golf round.
 * @return The calculated handicap as a double.
 */
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