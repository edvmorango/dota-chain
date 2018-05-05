package model

object Entities {

type UID = Option[Long]


case class Player(id: UID, name: String, nickname: String)

case class Team(id: UID, name: String, tag: String, players: List[Player])

case class Manager(id: UID, name: String)


}
