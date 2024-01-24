import Dependencies._

ThisBuild / scalaVersion     := "2.12.18"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "eventhub_to_datalake_etl",
    libraryDependencies += munit % Test
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.0"

// https://mvnrepository.com/artifact/com.microsoft.azure/azure-eventhubs-spark
libraryDependencies += "com.microsoft.azure" %% "azure-eventhubs-spark" % "2.3.22"
