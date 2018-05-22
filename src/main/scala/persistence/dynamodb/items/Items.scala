package persistence.dynamodb.items

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import model.Entities.{Manager, UID}
import persistence.dynamodb.syntax.DynamoItemParser
import persistence.dynamodb.utils.CreateTableUtil

case class ManagerItem(uid: UID, rid: String, name: String, nickname: String) {}

object ManagerItem {

  private val rk = "rangeKey"

  def toModel(i: ManagerItem): Manager =
    Manager.apply(i.uid, i.name, i.nickname)

  def fromModel(m: Manager): ManagerItem =
    ManagerItem(m.uid, rk, m.name, m.nickname)

  implicit val parser = new DynamoItemParser[ManagerItem] {

    private val fields =
      ManagerItem.getClass.getDeclaredFields.toList.map(_.toString)

    override def toMap(item: ManagerItem): Map[String, AttributeValue] = {
      fields.zip(item.productIterator.to).map {
        case (k, v: String)  => k -> new AttributeValue(v)
        case (k, v: Boolean) => k -> new AttributeValue(v)
      }

    }.toMap

  }

}

object Player
