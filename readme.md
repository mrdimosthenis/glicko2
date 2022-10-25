# glicko2

An implementation of the **Glicko-2** rating system for **Scala** and **Scala.js**.

## Install the library

Add the dependency in the `build.sbt` file.

```scala
libraryDependencies += "com.github.mrdimosthenis" %% "glicko2" % "1.0.1"
```

For Scala.js use the `%%%` operator instead of `%%`.

## Import the package

```scala
import dimos.glicko2._
```

## Create players

`Player` is a case class with three parameters (`rating`, `deviation`, `volatility`).

* We can create a player by defining their parameters.
```scala
val player = Player(1464, 151, 0.06)
```

* We can also create a player with the default initial parameters.
```scala
val newPlayer = Player.newEntry()
// Player(1500.0, 350.0, 0.06)
```

## Create results

`Result` is an enum with three values (`WonAgainst`, `DefeatedBy`, `TiedWith`).

To create results, we need to specify their kind, their opponent and place them inside a `Seq`.
```scala
val res = List(Result.WonAgainst(newPlayer))
```

## Calculate player params after a period of results

We can see how results affect a player.
```scala
val playerUpdated = player.afterPeriod(res)
// Player(1507.5, 145.3, 0.06)
```

## Glickman's example

Suppose a player rated `1500` competes against players rated `1400`, `1550` and `1700`, winning
the first game and losing the next two. Assume the `1500`-rated player’s rating deviation
is `200`, and his opponents’ are `30`, `100` and `300`, respectively. Assume the `1500` player has
volatility _σ_ = `0.06`, and his opponents have `0.07`, `0.08` and `0.05`, respectively.

Let's see how this example is translated and what happens to the first player when the period ends.
```scala
val p = Player(1500, 200, 0.06)
val opp1 = Player(1400, 30, 0.07)
val opp2 = Player(1550, 100, 0.08)
val opp3 = Player(1700, 300, 0.05)
val res1 = Result.WonAgainst(opp1)
val res2 = Result.DefeatedBy(opp2)
val res3 = Result.DefeatedBy(opp3)
val results = List(res1, res2, res3)
val pUpdated = p.afterPeriod(results)
// Player(1464.06, 151.52, 0.05999)
```

## Tuning

If the default values of the system do not serve us well, we can change them.
```scala
val tuning = Tuning.default(initRating = 1200, minDeviation = 30, tau = 0.75)
// Tuning(1200.0, 350.0, 30.0, 0.06, 0.75, 0.000001)
```

In that case, we need to pass `tuning` as a parameter to `newEntry` and `afterPeriod` functions.

These are the parameters of `Tuning` and their default values:
* `initRating`: 1500
* `maxDeviation`: 350,
* `minDeviation`: 0,
* `initVolatility`: 0.06
* `tau`: 0.5
* `tolerance`: 0.000001
