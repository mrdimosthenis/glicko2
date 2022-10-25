inThisBuild(List(
  organization := "com.github.mrdimosthenis",
  homepage := Some(url("https://github.com/mrdimosthenis/glicko2")),
  licenses := List("MIT" -> url("https://rem.mit-license.org/")),
  developers := List(
    Developer(
      "mrdimosthenis",
      "Dimos Michailidis",
      "mrdimosthenis@hotmail.com",
      url("https://github.com/mrdimosthenis")
    )
  )
))

lazy val root = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    scalaVersion := "3.1.1",
    name := "glicko2",
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    libraryDependencies ++= Seq(
      "io.monix" %%% "minitest" % "2.9.6" % "test",
      "org.scalacheck" %%% "scalacheck" % "1.15.4" % "test"
    )
  )
  .jvmSettings()
  .jsSettings(
    scalaJSUseMainModuleInitializer := false
  )
