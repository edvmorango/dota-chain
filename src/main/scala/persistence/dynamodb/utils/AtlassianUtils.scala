package persistence.dynamodb.utils

import io.atlassian.aws.dynamodb.{Column, Decoder, Encoder, TableDefinition}
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

object ManagerValue {

  val column = Column.compose2[ManagerValue](name, nickname) {

    case ManagerValue(n, ni) =>
  }

}

object Main extends App {

  TableDefinition.from[Key, HashKey, RangeKey, ManagerValue](
    "tb_manager",
    Key.column,
    ManagerValue.column,
    HashKey.name,
    ManagerValue.name)
}
