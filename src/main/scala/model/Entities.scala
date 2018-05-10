package model

object Entities {

  type UID = Option[String]

  case class Player(uid: UID, name: String, nickname: String)

  case class Team(uid: UID, name: String, tag: String, players: Set[Player])

  case class Manager(uid: UID, name: String)

}
