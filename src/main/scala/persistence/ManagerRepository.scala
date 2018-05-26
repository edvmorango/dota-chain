package persistence

import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits.{
  GetItem,
  PutItem,
  Query
}
import akka.stream.scaladsl.Source
import cats.effect.IO
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import model.Entities.{Manager, Player, UID}
import com.amazonaws.services.dynamodbv2.model._
import persistence.dynamodb.items.ManagerItem

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import persistence.dynamodb.parser.DynamoItemParserSyntax._
import persistence.dynamodb.DynamoDBClient._
import com.amazonaws.services.dynamodbv2.model._
import fs2.Pipe.Stepper.Await
import persistence.dynamodb.DynamoDBSetup

import scala.concurrent
import scala.concurrent.duration.DurationLong
import syntax.IOSyntax._

trait ManagerRepository extends GenericRepository[Manager]

case class ManagerRepositoryDynamo(tableName: String)
    extends ManagerRepository {

  import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._

  override def create(obj: Manager): IO[String] = {

    IO {

      val item = ManagerItem.fromModel(obj)
      val itemMap = item.toMap().asJava

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

  override def list(): IO[Seq[Manager]] = {

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

}

object ManagerRepositoryDynamo {

  val tableName = "tbl_manager"

  def apply: ManagerRepositoryDynamo = new ManagerRepositoryDynamo(tableName)

}

object RunIt extends App {

  val rep: ManagerRepository = ManagerRepositoryDynamo.apply

  val manager = Manager(None, "Igor", "ZeroGravity")

  val comp: IO[Unit] =
    for {
      _ <- DynamoDBSetup.setupDatabase()
      id <- rep.create(manager)
      oi <- rep.findById(id)
      l <- rep.list()
    } yield {

      println(oi)

      l.foreach(println)

      ()
    }

  comp.unsafeRunAsync { either =>
    if (either.isLeft)
      println(either.left.get.getMessage)

  }
  println("Finished")
}
