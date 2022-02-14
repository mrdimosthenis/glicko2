package dimos.glicko2

import org.scalacheck.Properties
import org.scalacheck.Prop

object PlayerSpecification extends Properties("Player"):

  private val errorTolerance = 0.000001

  property("minRating compared to rating") = Prop.forAll(Generators.player) { p =>
    p.minRating <= p.rating
  }

  property("maxRating compared to rating") = Prop.forAll(Generators.player) { p =>
    p.maxRating >= p.rating
  }

  property("rating after win") =
    Prop.forAll(Generators.player, Generators.player) { (p1, p2) =>
      p1.afterPeriod(List(Result.WonAgainst(p2))).rating + errorTolerance >= p1.rating
  }

  property("rating after loss") =
    Prop.forAll(Generators.player, Generators.player) { (p1, p2) =>
      p1.afterPeriod(List(Result.DefeatedBy(p2))).rating - errorTolerance <= p1.rating
  }

  property("rating after draw with itself") =
    Prop.forAll(Generators.player) { p =>
      val newRating = p.afterPeriod(List(Result.TiedWith(p))).rating
      Math.abs(newRating - p.rating) <= errorTolerance
  }

  property("player after empty period") =
    Prop.forAll(Generators.player) { p =>
      val Player(r, rd, vol) = p.afterPeriod(List())
      r == p.rating &&
        vol == p.volatility &&
        rd + errorTolerance >= p.deviation
  }
