package dimos.glicko2

import minitest._
import dimos.glicko2.glicko

object GlickoSuite extends SimpleTestSuite:

  test("mi") {
    assertEquals(
      glicko.mi(1500)(1500),
      0.0
    )

    assertEquals(
      glicko.mi(1500)(1400),
      -0.5756462492617337
    )

    assertEquals(
      glicko.mi(1500)(1550),
      0.28782312463086684
    )

    assertEquals(
      glicko.mi(1500)(1700),
      1.1512924985234674
    )
  }

  test("pfi") {
    assertEquals(
      glicko.pfi(200),
      1.1512924985234674
    )

    assertEquals(
      glicko.pfi(30),
      0.1726938747785201
    )

    assertEquals(
      glicko.pfi(100),
      0.5756462492617337
    )

    assertEquals(
      glicko.pfi(300),
      1.726938747785201
    )
  }

  test("gi") {
    assertEquals(
      glicko.gi(0.1727),
      0.9954976892391121
    )

    assertEquals(
      glicko.gi(0.5756),
      0.9531559851848544
    )

    assertEquals(
      glicko.gi(1.7269),
      0.724243204658613
    )
  }

  test("epsilon") {
    assertEquals(
      glicko.epsilon(0.0, -0.5756, 0.1727),
      0.6394570736821329
    )

    assertEquals(
      glicko.epsilon(0.0, 0.2878, 0.5756),
      0.4318472692194029
    )

    assertEquals(
      glicko.epsilon(0.0, 1.1513, 1.7269),
      0.3028377039664498
    )
  }

  test("upsilon and delta") {
    val mi_phi_s_js = List(
      (-0.5756, 0.1727, 1.0),
      (0.2878, 0.5756, 0.0),
      (1.1513, 1.7269, 0.0)
    )

    assertEquals(
      glicko.upsilon_with_delta(0.0, mi_phi_s_js),
      (1.778950432544242,-0.4839213192288748)
    )
  }

  test("a, b and f") {
    val (a, b, f) = glicko.a_b_f(
      0.06,
      1.1513,
      -0.4834,
      1.7785,
      0.5
    )

    assertEquals(
      (a, b, f(a), f(b)),
      (
        -5.626821433520073,
        -6.126821433520073,
        -0.0005356716868077427,
        1.9996749625881765
      )
    )
  }

  test("sigma sharp") {
    val expected = 0.0599959830198937
    val tolerance = 0.000001
    val result = glicko.sigma_sharp(
        0.06,
        -0.4834,
        1.7785,
        1.1513,
        0.5,
      tolerance
      )
    assert(
      Math.abs(expected - result) < tolerance
    )
  }

  test("pfi asterisk") {
    assertEquals(
      glicko.pfi_asterisk(1.1513, 0.05999),
      1.1528618694796007
    )
  }

  test("pfi sharp") {
    assertEquals(
      glicko.pfi_sharp( 1.152862, 1.7785),
      0.8721523318810543
    )
  }

  test("mi sharp") {
    val mi_phi_s_js = List(
      (-0.5756, 0.1727, 1.0),
      (0.2878, 0.5756, 0.0),
      (1.1513, 1.7269, 0.0)
    )

    assertEquals(
      glicko.mi_sharp(0.8722, 0.0, mi_phi_s_js),
      -0.20693934624531654
    )
  }

  test("r sharp") {
    assertEquals(
      glicko.r_sharp(1500)(-0.2069),
      1464.05778718
    )
  }

  test("rd sharp") {
    assertEquals(
      glicko.rd_sharp(0.8722),
      151.51666516
    )
  }

  test("calculate") {
    val mi_phi_s_js = List(
      (-0.5756, 0.1727, 1.0),
      (0.2878, 0.5756, 0.0),
      (1.1513, 1.7269, 0.0)
    )

    assertEquals(
      glicko.calculate(1500,0.5, 0.000001
      )(mi_phi_s_js)(
        1500, 200, 0.06
      ),
      (1500, 200, 0.06)
    )
  }
