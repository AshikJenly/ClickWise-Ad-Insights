name := """clickstreambackend"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
// play.sbt.routes.RoutesKeys.routesImport += "com.jenly.binders._"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"
// https://mvnrepository.com/artifact/org.playframework/play-slick
libraryDependencies += "org.playframework" %% "play-slick" % "6.0.0"
// https://mvnrepository.com/artifact/org.playframework/play-json
libraryDependencies += "org.playframework" %% "play-json" % "3.0.2"
// https://mvnrepository.com/artifact/com.typesafe.slick/slick
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.4.1"
