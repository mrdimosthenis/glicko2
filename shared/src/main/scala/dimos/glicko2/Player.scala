package dimos.glicko2

import dimos.glicko2.glicko
import scala.util.chaining._

case class Player(rating: Double, deviation: Double, volatility: Double):

  def minRating: Double = rating - 2 * deviation

  def maxRating: Double = rating + 2 * deviation

  def afterPeriod(tuning: Tuning = Tuning.default(),
                  results: Seq[Result]): Player =
    results
      .map {
        case Result.WonAgainst(p) => (1.0, p)
        case Result.DefeatedBy(p) => (0.0, p)
        case Result.TiedWith(p) => (0.5, p)
      }
      .map { case (s, p: Player) =>
        (p.rating, p.deviation, s)
      }
      .pipe{ r_rd_s_js =>
        glicko.calculate(
          tuning.initRating, tuning.tau, tuning.tolerance
        )(r_rd_s_js)(
          rating, deviation, volatility
        )
      }
      .pipe(Player(_, _, _))

object Player:

  def newEntry(tuning: Tuning = Tuning.default()): Player =
    Player(tuning.initRating, tuning.maxDeviation, tuning.initVolatility)
