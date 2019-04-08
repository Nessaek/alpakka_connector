name := "map-prep"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= {

  val datastaxVersion = "3.6.0"

  val akkaHttpVersion = "10.1.5"
  val logbackVersion = "1.2.3"

  val circeVersion = "0.11.1"
  Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",
    "com.datastax.cassandra" % "cassandra-driver-core" % datastaxVersion,
    "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "1.0-M3",
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )
}