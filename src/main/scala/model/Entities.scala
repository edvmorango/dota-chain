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
