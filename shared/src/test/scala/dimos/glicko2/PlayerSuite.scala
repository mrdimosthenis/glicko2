package dimos.glicko2

import minitest._
import dimos.glicko2.Player

object PlayerSuite extends SimpleTestSuite :

  test("min-max rating") {
    val playerA = Player(1850, 50, 0.06)

    assertEquals(playerA.minRating, 1750)
    assertEquals(playerA.maxRating, 1950)
  }

  test("afterPeriod player") {
    val player = Player(1500, 200, 0.06)
    val player2 = Player(1400, 30, 0.07)
    val player3 = Player(1550, 100, 0.08)
    val player4 = Player(1700, 300, 0.05)

    val result2 = Result.WonAgainst(player2)
    val result3 = Result.DefeatedBy(player3)
    val result4 = Result.DefeatedBy(player4)

    val results = List(result2, result3, result4)

    assertEquals(
      player.afterPeriod(results),
      Player(1464.0506705393013, 151.51652412385727, 0.059995984286488495)
    )
  }

  test("afterPeriod player - vector of results") {
    val player = Player(1500, 200, 0.06)
    val player2 = Player(1400, 30, 0.07)
    val player3 = Player(1550, 100, 0.08)
    val player4 = Player(1700, 300, 0.05)

    val result2 = Result.WonAgainst(player2)
    val result3 = Result.DefeatedBy(player3)
    val result4 = Result.DefeatedBy(player4)

    val results = Vector(result2, result3, result4)

    assertEquals(
      player.afterPeriod(results),
      Player(1464.0506705393013, 151.51652412385727, 0.059995984286488495)
    )
  }
