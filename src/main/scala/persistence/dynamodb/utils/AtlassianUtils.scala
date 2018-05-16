package persistence.dynamodb.utils

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import io.atlassian.aws.{AmazonClient, AmazonClientConnectionDef, Credential}
import io.atlassian.aws.dynamodb._
import model.Entities.Manager

case class HashKey(a: String)

object HashKey {

  implicit val hashKeyEncoder: Encoder[HashKey] =
    Encoder[String].contramap(k => k.a)

  implicit val hashKeyDecoder: Decoder[HashKey] =
    Decoder[String].collect(decodeHashKey)

  private lazy val decodeHashKey: PartialFunction[String, HashKey] = {
    case (a) => HashKey(a)
  }

  val name = Column[HashKey]("uid")

}

case class RangeKey(a: String)

object RangeKey {

  implicit val rangeKeyEncoder: Encoder[RangeKey] =
    Encoder[String].contramap(k => k.a)

  implicit val rangeKeyDecoder: Decoder[RangeKey] =
    Decoder[String].collect(decodeRangeKey)

  private lazy val decodeRangeKey: PartialFunction[String, RangeKey] = {
    case (a) => RangeKey(a)
  }

  val name = Column[RangeKey]("rangeKey")

}

case class Key(h: String, r: String)

object Key {

  lazy val column =
    Column.compose2[Key](HashKey.name.column, RangeKey.name.column) {
      case Key(a, b) => (HashKey(a), RangeKey(b))
    } {
      case (HashKey(a), RangeKey(b)) => Key(a, b)
    }

}

case class ManagerValue(name: String, nickname: String)

object ManagerValueMapping {

  val hashKey: NamedColumn[HashKey] = HashKey.name
  val rangeKey: NamedColumn[RangeKey] = RangeKey.name
  val name = Column[String]("name").column
  val nickname = Column[String]("nickname").column

}

object ManagerValue {
  import ManagerValueMapping._
  val column = Column.compose2[ManagerValue](name, nickname) {
    case ManagerValue(n, ni) => (n, ni)
  } { ManagerValue.apply }

}

object Main extends App {
  import DynamoDBAction._

  val credentials: Credential = Credential.static("user", "123")

  val endpoint: AmazonClientConnectionDef = AmazonClientConnectionDef.default
    .copy(endpointUrl = Some("http://localhost:8000"),
          credential = Some(credentials))

  val client: AmazonDynamoDBClient =
    DynamoDBClient.withClientConfiguration(endpoint, None, None)

  val tb = TableDefinition
    .from[Key, ManagerValue, HashKey, RangeKey]("tb_manager",
                                                Key.column,
                                                ManagerValue.column,
                                                HashKey.name,
                                                RangeKey.name)

//  DynamoDB.cre
//  AmazonDynamoDb
//  DynamoDB.client.client.createTable(tb)

}
