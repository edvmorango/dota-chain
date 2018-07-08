package persistence

import cats.effect.IO
import model.Entities.{Player, Team}
import syntax.IOSyntax._

import scala.concurrent.ExecutionContext.Implicits.global

trait TeamRepository extends GenericRepository[Team] {

  def findByTag(tag: String): IO[Option[Team]]

  def findByPlayerNickname(nickname: String): IO[Option[Team]]

}

case class DynamoDBTeamRepository(tableName: String,
                                  playerRepository: PlayerRepository)
    extends TeamRepository {

  import persistence.dynamodb.ItemMapper._
  import persistence.dynamodb.DynamoDBClient._
  import com.amazonaws.services.dynamodbv2.model._
  import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
  import scala.collection.JavaConverters._
  import com.amazonaws.services.dynamodbv2.model.PutItemRequest
  import persistence.dynamodb.TeamItem

  def create(obj: Team): IO[String] = {

    IO {
      val item = TeamItem(obj)
      val itemMap = item.asJKV()

      instance
        .single(
          new PutItemRequest()
            .withTableName(tableName)
            .withItem(itemMap))
        .map(_ => item.uid)
    }.flatIO

  }

  def findById(id: String): IO[Option[Team]] = {
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
            .map(_.asItem[TeamItem].asModel())
            .headOption)
    }.flatIO

  }

  def findByTag(tag: String): IO[Option[Team]] = {

    IO {

      val key = Map(":tag" -> new AttributeValue().withS(tag)).asJava

      val request =
        new ScanRequest()
          .withTableName(tableName)
          .withFilterExpression("tag = :tag")
          .withExpressionAttributeValues(key)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(_.asItem[TeamItem].asModel())
            .headOption)

    }.flatIO

  }

  def list(): IO[Seq[Team]] = {

    IO {

      val request = new ScanRequest().withTableName(tableName)

      instance
        .single(request)
        .map(
          _.getItems.asScala
            .map(_.asItem[TeamItem].asModel())
            .toSeq)

    }.flatIO

  }

  def findByPlayerNickname(nickname: String): IO[Option[Team]] = {
    import io.circe.generic.auto._
    import io.circe.syntax._

    val result: IO[Option[Team]] = for {
      playerOpt <- playerRepository.findByNickname(nickname)
      team <- playerOpt match {
        case Some(player) =>
          IO {
            val key =
              Map(
                ":player" -> new AttributeValue().withS(
                  player.asJson.toString())).asJava

            val request =
              new ScanRequest()
                .withTableName(tableName)
                .withFilterExpression("contains (players,:player)")
                .withExpressionAttributeValues(key)

            instance
              .single(request)
              .map(
                _.getItems.asScala
                  .map(_.asItem[TeamItem].asModel())
                  .headOption)
          }.flatIO()

        case None => IO.pure[Option[Team]](None)
      }

    } yield team

    result
  }
}

object DynamoDBTeamRepository {

  val tableName = "tbl_team"

  def apply =
    new DynamoDBTeamRepository(tableName, DynamoDBPlayerRepository.apply)

  def apply(playerRepository: PlayerRepository) =
    new DynamoDBTeamRepository(tableName, playerRepository)

}
