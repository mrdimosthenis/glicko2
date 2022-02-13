package dimos.glicko2

import minitest._
import dimos.glicko2.Player

object PlayerSuite extends SimpleTestSuite:

  test("min-max rating calculation") {
    val playerA = Player(1850, 50, 0.06)

    assertEquals(1750, playerA.minRating)
    assertEquals(1950, playerA.maxRating)
  }

  test("afterPeriod player calculation") {
    val tuning = Tuning.default(tau = 0.5)

    val player = Player(1500, 200, 0.06)
    val player2 = Player(1400, 30, 0.07)
    val player3 = Player(1550, 100, 0.08)
    val player4 = Player(1700, 300, 0.05)

    val result1 = Result.WonAgainst(player2)
    val result2 = Result.DefeatedBy(player3)
    val result3 = Result.DefeatedBy(player4)

    val results = List(result1, result2, result3)

    assertEquals(
      Player(1464.06, 151.52, 0.05999),
      player.afterPeriod(tuning, results)
    )
  }




