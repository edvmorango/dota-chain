package persistence.dynamodb

import java.util.UUID

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import model.Entities.{Manager, Player, Team}
import model.{ManagerAlgebra, PlayerAlgebra, TeamAlgebra}
import scala.collection.JavaConverters._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
sealed trait ItemAlgebra[+A] {

  def asModel(): A

}

package object ItemMapper {

  import scala.collection.JavaConverters._
  import io.circe.generic.auto._
  import io.circe.parser.decode

  implicit class ItemMappers[A <: ItemAlgebra[_]](item: A) {

    def asJKV(): java.util.Map[String, AttributeValue] = {

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

  }

  implicit class KVMapper[A <: ItemAlgebra[_]](
      kv: java.util.Map[String, AttributeValue]) {
    def asItem[A]()(
        implicit cv: java.util.Map[String, AttributeValue] => A): A =
      cv(kv)

  }

}

case class ManagerItem(uid: String, rid: String, name: String, nickname: String)
    extends ItemAlgebra[ManagerAlgebra] {

  def asModel(): Manager = Manager(Option(uid), name, nickname)

}

object ManagerItem {

  private val rk = "rangeKey"

  implicit val cv: java.util.Map[String, AttributeValue] => ManagerItem = { m =>
    val map = m.asScala
    ManagerItem(map("uid").getS,
                map("rid").getS,
                map("name").getS,
                map("nickname").getS)
  }

  def apply(alg: ManagerAlgebra): ManagerItem = alg match {
    case m: Manager =>
      new ManagerItem(m.uid.getOrElse(UUID.randomUUID().toString),
                      rk,
                      m.name,
                      m.nickname)
  }

}

case class PlayerItem(uid: String, rid: String, name: String, nickname: String)
    extends ItemAlgebra[PlayerAlgebra] {

  def asModel(): Player = Player(Option(uid), name, nickname)

}

object PlayerItem {

  private val rk = "rangeKey"

  implicit val cv: java.util.Map[String, AttributeValue] => PlayerItem = { m =>
    val map = m.asScala
    PlayerItem(map("uid").getS,
               map("rid").getS,
               map("name").getS,
               map("nickname").getS)
  }

  def apply(alg: PlayerAlgebra): PlayerItem = alg match {
    case m: Player =>
      new PlayerItem(m.uid.getOrElse(UUID.randomUUID().toString),
                     rk,
                     m.name,
                     m.nickname)
  }

}

case class TeamItem(uid: String,
                    rid: String,
                    name: String,
                    tag: String,
                    players: Set[String])
    extends ItemAlgebra[TeamAlgebra] {

  def asModel(): Team =
    Team(Option(uid), name, tag, players.map(x => decode[Player](x).right.get))

}

object TeamItem {

  private val rk = "rangeKey"

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

  implicit val cv: java.util.Map[String, AttributeValue] => TeamItem = { m =>
    val map = m.asScala
    val players =
      if (map.contains("players")) map("players").getSS.asScala.toSet
      else Set[String]()
    TeamItem(map("uid").getS,
             map("rid").getS,
             map("name").getS,
             map("tag").getS,
             players)
  }

  def apply(alg: TeamAlgebra): TeamItem = alg match {
    case m: Team =>
      new TeamItem(m.uid.getOrElse(UUID.randomUUID().toString),
                   rk,
                   m.name,
                   m.tag,
                   m.players.map(_.asJson.toString()))
  }

}
