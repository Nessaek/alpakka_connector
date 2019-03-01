name := "map-prep"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= {

  val datastaxVersion = "3.6.0"

  val akkaHttpVersion = "10.1.5"
  val logbackVersion = "1.2.3"

  Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",
    "com.datastax.cassandra" % "cassandra-driver-core" % datastaxVersion
  )


}