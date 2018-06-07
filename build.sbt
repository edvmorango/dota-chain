name := "dota-chain"

version := "0.1"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.18.10",
  "org.http4s" %% "http4s-blaze-server" % "0.18.10",
  "org.http4s" %% "http4s-blaze-client" % "0.18.10",
  "org.http4s" %% "http4s-circe" % "0.18.10",
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-literal" % "0.9.3",
  "io.circe" %% "circe-parser" %  "0.9.3",
  "com.github.firebase4s" %% "firebase4s" % "0.0.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
)

scalacOptions ++= Seq("-Ypartial-unification")
scalacOptions ++= Seq("-language:higherKinds")