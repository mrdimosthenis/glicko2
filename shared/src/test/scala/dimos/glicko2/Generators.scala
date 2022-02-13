package dimos.glicko2

import org.scalacheck.Gen

object Generators:

  val posDouble: Gen[Double] =
    Gen.chooseNum(0.0, Double.MaxValue)

  val player: Gen[Player] = for {
    rating <- posDouble
    deviation <- posDouble
    volatility <- posDouble
  } yield Player(rating, deviation, volatility)
