ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

val minitestVersion = "2.9.6"

lazy val root = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    name := "glicko2",
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    libraryDependencies ++= Seq(
      "io.monix" %%% "minitest" % minitestVersion % "test",
      "org.scalacheck" %%% "scalacheck" % "1.15.4"
    )
  )
  .jvmSettings()
  .jsSettings(
    scalaJSUseMainModuleInitializer := false
  )
