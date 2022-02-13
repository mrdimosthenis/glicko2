package dimos.glicko2

case class Tuning(initRating: Double,
                  maxDeviation: Double,
                  minDeviation: Double,
                  initVolatility: Double,
                  tau: Double,
                  tolerance: Double)

object Tuning:

  def default(initRating: Double = 1500,
              maxDeviation: Double = 350,
              minDeviation: Double = 30,
              initVolatility: Double = 0.06,
              tau: Double = 0.75,
              tolerance: Double = 0.000001): Tuning =
    Tuning(initRating, maxDeviation, minDeviation, initVolatility, tau, tolerance)
