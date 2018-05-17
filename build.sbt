name := "dota-chain"

version := "0.1"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.18.10",
  "org.http4s" %% "http4s-blaze-server" % "0.18.10",
  "org.http4s" %% "http4s-blaze-client" % "0.18.10",
  "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb" % "0.19"
)

scalacOptions ++= Seq("-Ypartial-unification")