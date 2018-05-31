package persistence

import cats.effect.IO
import model.Entities.Manager

import scala.concurrent.ExecutionContext.Implicits.global
import syntax.IOSyntax._

trait ManagerRepository extends GenericRepository[Manager] {

  def findByNickname(nickname: String): IO[Option[Manager]]

}

case class DynamoDBManagerRepository(tableName: String)
    extends ManagerRepository {

  import persistence.dynamodb.ItemMapper._
  import persistence.dynamodb.DynamoDBClient._
  import com.amazonaws.services.dynamodbv2.model._
  import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
  import persistence.dynamodb.ManagerItem

  import scala.collection.JavaConverters._

  override def create(obj: Manager): IO[String] = {

    IO {
      val item = ManagerItem.fromModel(obj)
      val itemMap = item.toJKV()

      val xd = item.toModel()

      println(xd)

      instance
        .single(
          new PutItemRequest()
            .withTableName(tableName)
            .withItem(itemMap))
        .map(_ => item.uid)
    }.flatIO()

  }

  def findById(id: String): IO[Option[Manager]] = {

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
            .map(i => ManagerItem.modelFromMap(i.asScala.toMap))
            .headOption)
    }.flatIO

  }

  def list(): IO[Seq[Manager]] = {

    IO {

      val request = new ScanRequest().withTableName(tableName)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(i => ManagerItem.modelFromMap(i.asScala.toMap))
            .toSeq)

    }.flatIO()

  }

  def findByNickname(nickname: String): IO[Option[Manager]] = {

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
            .map(i => ManagerItem.modelFromMap(i.asScala.toMap))
            .headOption)

    }.flatIO()

  }

}

object DynamoDBManagerRepository {

  val tableName = "tbl_manager"

  def apply: DynamoDBManagerRepository =
    new DynamoDBManagerRepository(tableName)

}
