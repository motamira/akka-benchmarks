name := """akka-bench"""

mainClass in (Compile, run) := Some("pt.aigx.routing.RouterApplication")

version := "1.0"

scalaVersion := "2.11.1"

javacOptions ++= Seq("-source", "1.7")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "com.typesafe.akka" %% "akka-remote" % "2.3.6",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test")