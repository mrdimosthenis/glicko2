package dimos.glicko2

import scala.util.chaining._

package object glicko:

  private val scale = 173.7178

  def mi(init_r: Double)(r: Double): Double = (r - init_r) / scale

  def pfi(rd: Double): Double = rd / scale

  def r_sharp(init_r: Double)(mi_sharp: Double): Double = scale * mi_sharp + init_r

  def rd_sharp(pfi_sharp: Double): Double = scale * pfi_sharp

  def gi(phi_i: Double): Double =
    (3.0 * phi_i * phi_i)
      .pipe(_ / (Math.PI * Math.PI))
      .pipe(_ + 1.0)
      .pipe(Math.sqrt)
      .pipe(1.0 / _)

  def epsilon(mi_i: Double, mi_j: Double, phi_j: Double): Double =
    (0.0 - gi(phi_j))
      .pipe(_ * (mi_i - mi_j))
      .pipe(Math.exp)
      .pipe(_ + 1.0)
      .pipe(1.0 / _)

  def upsilon_with_delta(mi_i: Double, mi_phi_s_js: Seq[(Double, Double, Double)])
  : (Double, Double) =
    val u = mi_phi_s_js
      .map {
        case (mi_j, phi_j, s_j) => (mi_j, phi_j)
      }
      .map {
        case (mi_j, phi_j) =>
          val g = gi(phi_j)
          val e = epsilon(mi_i, mi_j, phi_j)
          g * g * e * (1.0 - e)
      }
      .sum
      .pipe(Math.pow(_, -1.0))
    val d = mi_phi_s_js
      .map {
        case (mi_j, phi_j, s_j) =>
          gi(phi_j) * (s_j - epsilon(mi_i, mi_j, phi_j))
      }
      .sum
      .pipe(_ * u)
    (u, d)

  private def ef(phi_pow_2: Double,
                 d_pow_2: Double,
                 u: Double,
                 a: Double,
                 tau: Double)
                (x: Double) =
    val e_pow_x = Math.exp(x)
    val minuend =
      (e_pow_x * (d_pow_2 - phi_pow_2 - u - e_pow_x)) /
        (2.0 * Math.pow(phi_pow_2 + u + e_pow_x, 2.0))
    val subtrahend = (x - a) / (tau * tau)
    minuend - subtrahend

  def a_b_f(sigma: Double,
            phi_i: Double,
            d: Double,
            u: Double,
            tau: Double)
  : (Double, Double, Double => Double) =
    val a = Math.log(sigma * sigma)
    val phi_pow_2 = phi_i * phi_i
    val d_pow_2 = d * d
    val f = ef(phi_pow_2, d_pow_2, u, a, tau)
    val diff = d_pow_2 - phi_pow_2 - u
    val b =
      if diff > 0
      then
        Math.log(diff)
      else
        LazyList
          .from(1)
          .map(a - _ * tau)
          .dropWhile(f(_) < 0)
          .head
    (a, b, f)

  def sigma_sharp(sigma: Double,
                  d: Double,
                  u: Double,
                  phi_i: Double,
                  tau: Double,
                  tolerance: Double)
  : Double =
    val (a, b, f) = a_b_f(sigma, phi_i, d, u, tau)
    val (final_a, _, _, _) =
      LazyList
        .iterate(
          (a, f(a), b, f(b))
        ) { (temp_a, temp_fa, temp_b, temp_fb) =>
          val c = temp_a + (temp_a - temp_b) * temp_fa / (temp_fb - temp_fa)
          val fc = f(c)
          val (next_a, next_fa) =
            if fc * temp_fb < 0
            then (temp_b, temp_fb)
            else (temp_a, temp_fa / 2.0)
          val (next_b, next_fb) = (c, fc)
          (next_a, next_fa, next_b, next_fb)
        }
        .dropWhile { (temp_a, _, temp_b, _) =>
          Math.abs(temp_b - temp_a) > tolerance
        }
        .head
    Math.exp(final_a / 2.0)

  def pfi_asterisk(pfi_i: Double, sigma_sh: Double): Double =
    Math.sqrt(pfi_i * pfi_i + sigma_sh * sigma_sh)

  def pfi_sharp(pfi_i_ast: Double, u: Double): Double =
    ((1.0 / (pfi_i_ast * pfi_i_ast)) + (1.0 / u))
      .pipe(Math.sqrt)
      .pipe(1.0 / _)

  def mi_sharp(pfi_i_sharp: Double,
               mi_i: Double,
               mi_phi_s_js: Seq[(Double, Double, Double)])
  : Double =
    mi_phi_s_js
      .map {
        case (mi_j, phi_j, s_j) =>
          gi(phi_j) * (s_j - epsilon(mi_i, mi_j, phi_j))
      }
      .sum
      .pipe(_ * pfi_i_sharp * pfi_i_sharp)
      .pipe(mi_i + _)

  def calculate(init_r: Double, tau: Double, tolerance: Double)
               (r_rd_s_js: Seq[(Double, Double, Double)])
               (r: Double, rd: Double, sigma: Double):
  (Double, Double, Double) =
    if r_rd_s_js.isEmpty
    then
      rd.pipe(pfi)
        .pipe(pfi_asterisk(_, sigma))
        .pipe(rd_sharp)
        .pipe((r, _, sigma))
    else
      val mi_i = mi(init_r)(r)
      val phi_i = pfi(rd)
      val mi_phi_s_js = r_rd_s_js.map { case (r_j, rd_j, sigma_j) =>
        (mi(init_r)(r_j), pfi(rd_j), sigma_j)
      }
      val (u, d) = upsilon_with_delta(mi_i, mi_phi_s_js)
      val sigma_sh = sigma_sharp(sigma, d, u, phi_i, tau, tolerance)
      val phi_i_ast = pfi_asterisk(phi_i, sigma_sh)
      val phi_i_sh = pfi_sharp(phi_i_ast, u)
      val mi_i_sh = mi_sharp(phi_i_sh, mi_i, mi_phi_s_js)
      val r_sh = r_sharp(init_r)(mi_i_sh)
      val rd_sh = rd_sharp(phi_i_sh)
      (r_sh, rd_sh, sigma_sh)
