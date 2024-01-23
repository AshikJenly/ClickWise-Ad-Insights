import Dependencies._

ThisBuild / scalaVersion     := "2.12.18"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "datalake_to_cosmosdb",
    libraryDependencies += munit % Test
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.0"

