package dimos.glicko2

enum Result:
  case WonAgainst(player: Player)
  case DefeatedBy(player: Player)
  case TiedWith(player: Player)
