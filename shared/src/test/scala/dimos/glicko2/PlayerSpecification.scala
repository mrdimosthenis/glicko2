package dimos.glicko2

import org.scalacheck.Properties
import org.scalacheck.Prop

object PlayerSpecification extends Properties("Player"):

  property("minRating") = Prop.forAll(Generators.player) { p =>
    p.minRating <= p.rating
  }

  property("maxRating") = Prop.forAll(Generators.player) { p =>
    p.maxRating >= p.rating
  }
