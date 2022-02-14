package dimos.glicko2

import org.scalacheck.Gen

object Generators:

  val rating: Gen[Double] =
    Gen.chooseNum(0.0, 4000.0)

  val deviation: Gen[Double] =
    Gen.chooseNum(0.0, 350.0)

  val volatility: Gen[Double] =
    Gen.chooseNum(0.0, 1.0)

  val player: Gen[Player] = for {
    r <- rating
    rd <- deviation
    vol <- volatility
  } yield Player(r, rd, vol)
