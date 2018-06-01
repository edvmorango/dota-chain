package model

import cats.effect.IO
import org.http4s.circe.jsonOf
import io.circe._
import io.circe.generic.semiauto._
import model.Entities.Player

sealed trait PlayerAlgebra
sealed trait TeamAlgebra
sealed trait ManagerAlgebra

object Entities {

  type UID = Option[String]

  case class Player(uid: UID, name: String, nickname: String)
      extends PlayerAlgebra

  case class Team(uid: UID, name: String, tag: String, players: Set[Player])
      extends TeamAlgebra

  case class Manager(uid: UID, name: String, nickname: String)
      extends ManagerAlgebra

}

object Main extends App {
  import shapeless._
  import record._
  import syntax.singleton._

  val g = LabelledGeneric[PlayerAlgebra]
  val p = Player(None, "penis", "penis 2")

  val nil = g.to(p)

  val from = g.from(nil)

  println(nil)
  println(from)

}
