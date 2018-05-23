package persistence.dynamodb.items

import java.nio.ByteBuffer
import java.util.{Date, UUID}

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import model.Entities.{Manager, UID}
import persistence.dynamodb.syntax.DynamoItemParser
import persistence.dynamodb.utils.CreateTableUtil

case class ManagerItem(uid: String, rid: String, name: String, nickname: String)

object ManagerItem {

  private val rk = "rangeKey"

  def toModel(i: ManagerItem): Manager =
    Manager.apply(Option(i.uid), i.name, i.nickname)

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

    override def toMap(item: ManagerItem): Map[String, AttributeValue] = {
      Map(
        "uid" -> new AttributeValue(item.uid),
        "rid" -> new AttributeValue(item.rid),
        "name" -> new AttributeValue(item.name),
        "nickname" -> new AttributeValue(item.nickname)
      )
    }
  }
}
