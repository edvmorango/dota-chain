package persistence.dynamodb.items

import java.nio.ByteBuffer
import java.util.{Date, UUID}

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import model.Entities.{Manager, Player, Team, UID}
import persistence.dynamodb.parser.DynamoItemParser
import persistence.dynamodb.utils.CreateTableUtil

case class ManagerItem(uid: String, rid: String, name: String, nickname: String)

object ManagerItem {

  private val rk = "rangeKey"

  def toModel(i: ManagerItem): Manager =
    Manager(Option(i.uid), i.name, i.nickname)

  def fromModel(m: Manager): ManagerItem = {
    val id = m.uid.getOrElse(UUID.randomUUID().toString)
    ManagerItem(id, rk, m.name, m.nickname)
  }

  def itemFromMap(map: Map[String, AttributeValue]): ManagerItem = {
    ManagerItem(map("uid").getS,
                map("rid").getS,
                map("name").getS,
                map("nickname").getS)
  }

  def modelFromMap(map: Map[String, AttributeValue]): Manager =
    toModel(itemFromMap(map))

  implicit val parser = new DynamoItemParser[ManagerItem] {

    def toMap(item: ManagerItem): Map[String, AttributeValue] = {
      Map(
        "uid" -> new AttributeValue(item.uid),
        "rid" -> new AttributeValue(item.rid),
        "name" -> new AttributeValue(item.name),
        "nickname" -> new AttributeValue(item.nickname)
      )
    }
  }
}

case class PlayerItem(uid: String, rid: String, name: String, nickname: String)

object PlayerItem {

  private val rk = "rangeKey"

  def toModel(i: PlayerItem): Player =
    Player(Option(i.uid), i.name, i.nickname)

  def fromModel(p: Player): PlayerItem = {
    val id = p.uid.getOrElse(UUID.randomUUID().toString)
    PlayerItem(id, rk, p.name, p.nickname)
  }

  def itemFromMap(map: Map[String, AttributeValue]): PlayerItem = {
    PlayerItem(map("uid").getS,
               map("rid").getS,
               map("name").getS,
               map("nickname").getS)
  }

  def modelFromMap(map: Map[String, AttributeValue]): Player =
    toModel(itemFromMap(map))

  implicit val parser = new DynamoItemParser[PlayerItem] {

    def toMap(item: PlayerItem): Map[String, AttributeValue] = {
      Map(
        "uid" -> new AttributeValue(item.uid),
        "rid" -> new AttributeValue(item.rid),
        "name" -> new AttributeValue(item.name),
        "nickname" -> new AttributeValue(item.nickname)
      )
    }
  }

}

case class TeamItem(uid: String,
                    rid: String,
                    name: String,
                    tag: String,
                    players: Set[String])

object TeamItem {
  import io.circe.generic.auto._, io.circe.syntax._, io.circe.parser.decode
  import scala.collection.JavaConverters._

  private val rk = "rangeKey"

  def toModel(i: TeamItem): Team =
    Team(Option(i.uid),
         i.name,
         i.tag,
         i.players.map(x => decode[Player](x).right.get))

  def fromModel(p: Team): TeamItem = {
    val id = p.uid.getOrElse(UUID.randomUUID().toString)
    TeamItem(id, rk, p.name, p.tag, p.players.map(_.asJson.toString()))
  }
//
  def itemFromMap(map: Map[String, AttributeValue]): TeamItem = {
    TeamItem(map("uid").getS,
             map("rid").getS,
             map("name").getS,
             map("tag").getS,
             map("players").getSS.asScala.toSet)
  }
//
//  def modelFromMap(map: Map[String, AttributeValue]): Team =
//    toModel(itemFromMap(map))
//
//  implicit val parser = new DynamoItemParser[TeamItem] {
//
//    def toMap(item: TeamItem): Map[String, AttributeValue] = {
//      Map(
//        "uid" -> new AttributeValue(item.uid),
//        "rid" -> new AttributeValue(item.rid),
//        "name" -> new AttributeValue(item.name),
//        "nickname" -> new AttributeValue(item.nickname)
//      )
//    }
//  }

}
