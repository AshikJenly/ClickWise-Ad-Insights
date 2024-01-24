import Dependencies._

ThisBuild / scalaVersion     := "2.12.18"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "EventHubToWarehouse",
    libraryDependencies += munit % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.0"
