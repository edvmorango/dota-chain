package persistence.dynamodb

import java.util.UUID

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import model.Entities.{Manager, Player, Team}
import model.{ManagerAlgebra, PlayerAlgebra, TeamAlgebra}
import shapeless.{Coproduct, Poly1}

sealed trait ItemAlgebra

case class ManagerItem(uid: String, rid: String, name: String, nickname: String)
    extends ItemAlgebra

package object ItemMapper {

  import scala.collection.JavaConverters._
  import shapeless.:+:
  import shapeless._
  import shapeless.ops._
  import shapeless.ops.coproduct._
  import io.circe.generic.auto._
  import io.circe.parser.decode
  import io.circe.syntax._

  type Model = PlayerAlgebra :+: TeamAlgebra :+: ManagerAlgebra

  private object modelMorphisms extends Poly1 {

    implicit def player = at[PlayerAlgebra]
    implicit def player2 = at[Player]
    implicit def team = at[TeamAlgebra]
    implicit def team2 = at[Team]

    implicit def manager = at[ManagerAlgebra]
    implicit def manager2 = at[Manager]
  }

  implicit class ItemMappers(item: ItemAlgebra) {

    def toJKV(): java.util.Map[String, AttributeValue] = {

      item match {
        case i: ManagerItem =>
          Map(
            "uid" -> new AttributeValue(i.uid),
            "rid" -> new AttributeValue(i.rid),
            "name" -> new AttributeValue(i.name),
            "nickname" -> new AttributeValue(i.nickname)
          ).asJava
        case i: PlayerItem =>
          Map(
            "uid" -> new AttributeValue(i.uid),
            "rid" -> new AttributeValue(i.rid),
            "name" -> new AttributeValue(i.name),
            "nickname" -> new AttributeValue(i.nickname)
          ).asJava
        case i: TeamItem => {
          val kv = Map(
            "uid" -> new AttributeValue(i.uid),
            "rid" -> new AttributeValue(i.rid),
            "name" -> new AttributeValue(i.name),
            "tag" -> new AttributeValue(i.tag)
          )

          if (i.players.isEmpty)
            kv.asJava
          else
            kv.+("players" -> new AttributeValue(i.players.toList.asJava))
              .asJava
        }
      }

    }

    def toModel[A](): A = {
      val res = item match {
        case i: ManagerItem =>
          Coproduct[Model] apply Manager(Option(i.uid), i.name, i.nickname)
        case i: PlayerItem =>
          Coproduct[Model] apply Player(Option(i.uid), i.name, i.nickname)
        case i: TeamItem =>
          Coproduct[Model] apply Team(
            Option(i.uid),
            i.name,
            i.tag,
            i.players.map(x => decode[Player](x).right.get))
      }
      res.select[A].get
    }
  }
}

object ManagerItem {

  private val rk = "rangeKey"

  implicit class ItemToModel(i: ManagerItem) {
    def toModel(i: ManagerItem) = Manager(Option(i.uid), i.name, i.nickname)
  }

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

}

case class PlayerItem(uid: String, rid: String, name: String, nickname: String)
    extends ItemAlgebra

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

}

case class TeamItem(uid: String,
                    rid: String,
                    name: String,
                    tag: String,
                    players: Set[String])
    extends ItemAlgebra

object TeamItem {

  import scala.collection.JavaConverters._
  import io.circe.generic.auto._
  import io.circe.parser.decode
  import io.circe.syntax._

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

  def itemFromMap(map: Map[String, AttributeValue]): TeamItem = {

    val players =
      if (map.contains("players")) map("players").getSS.asScala.toSet
      else Set[String]()
    TeamItem(map("uid").getS,
             map("rid").getS,
             map("name").getS,
             map("tag").getS,
             players)
  }

  def modelFromMap(map: Map[String, AttributeValue]): Team =
    toModel(itemFromMap(map))

}
