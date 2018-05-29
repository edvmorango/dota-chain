package persistence

import cats.effect.IO
import model.Entities.Player
import syntax.IOSyntax._
import scala.concurrent.ExecutionContext.Implicits.global

trait PlayerRepository extends GenericRepository[Player] {

  def findByNickname(nickname: String): IO[Option[Player]]

}

case class DynamoDBPlayerRepository(tableName: String)
    extends PlayerRepository {

  import persistence.dynamodb.parser.DynamoItemParserSyntax._
  import persistence.dynamodb.DynamoDBClient._
  import com.amazonaws.services.dynamodbv2.model._
  import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
  import persistence.dynamodb.items.PlayerItem
  import scala.collection.JavaConverters._
  import com.amazonaws.services.dynamodbv2.model.PutItemRequest

  def create(obj: Player): IO[String] = {

    IO {
      val item = PlayerItem.fromModel(obj)
      val itemMap = item.toMap().asJava

      instance
        .single(
          new PutItemRequest()
            .withTableName(tableName)
            .withItem(itemMap))
        .map(_ => item.uid)
    }.flatIO()

  }

  def findById(id: String): IO[Option[Player]] = {
    IO {

      val key = Map(":uid" -> new AttributeValue().withS(id)).asJava

      val request = new QueryRequest()
        .withTableName(tableName)
        .withKeyConditionExpression("uid = :uid")
        .withExpressionAttributeValues(key)
        .withLimit(1)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(i => PlayerItem.modelFromMap(i.asScala.toMap))
            .headOption)
    }.flatIO

  }

  override def findByNickname(nickname: String): IO[Option[Player]] = {

    IO {

      val key = Map(":nickname" -> new AttributeValue().withS(nickname)).asJava

      val request =
        new ScanRequest()
          .withTableName(tableName)
          .withFilterExpression("nickname = :nickname")
          .withExpressionAttributeValues(key)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(i => PlayerItem.modelFromMap(i.asScala.toMap))
            .headOption)

    }.flatIO()

  }

  def list(): IO[Seq[Player]] = {

    IO {

      val request = new ScanRequest().withTableName(tableName)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(i => PlayerItem.modelFromMap(i.asScala.toMap))
            .toSeq)

    }.flatIO()

  }
}

object DynamoDBPlayerRepository {

  val tableName = "tbl_player"

  def apply = new DynamoDBPlayerRepository(tableName)

}
