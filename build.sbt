//*** Metadata for the build

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "it.unich.jppl"
ThisBuild / description  := "Java bindings for the Parma Polyhedra Library"
ThisBuild / licenses     := Seq("GPL-3.0" -> url("https://opensource.org/licenses/GPL-3.0"))
ThisBuild / homepage     := Some(url("https://github.com/amato-gianluca/JPPL"))
ThisBuild / startYear    := Some(2022)
ThisBuild / developers    := List(
  Developer(
    "amato",
    "Gianluca Amato", "gianluca.amato@unich.it",
    url("http://www.sci.unich.it/~amato/")
  )
)

ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/amato-gianluca/JPPL"),
  "scm:git:https://github.com/amato-gianluca/JPPL.git",
  Some("scm:git:https://github.com/amato-gianluca/JPPL.git")
))

//*** Java configuration

ThisBuild / javacOptions ++= Seq(
  "-Xlint:deprecation"
)

//*** Build system configuration

ThisBuild / crossPaths := false
ThisBuild / autoScalaLibrary := false

//*** Project 

lazy val jppl = (project in file("."))
  .settings(
      libraryDependencies ++= Seq(
        "net.java.dev.jna" % "jna" % "5.11.0"
      )
  )
