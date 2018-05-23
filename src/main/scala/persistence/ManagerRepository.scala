package persistence

import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits.{GetItem, PutItem}
import akka.stream.scaladsl.Source
import cats.effect.IO
import model.Entities.{Manager, Player, UID}
import com.amazonaws.services.dynamodbv2.model._
import persistence.dynamodb.items.ManagerItem

import scala.collection.JavaConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import persistence.dynamodb.syntax.DynamoItemParserSyntax._
import persistence.dynamodb.DynamoDBClient._
import com.amazonaws.services.dynamodbv2.model._

trait ManagerRepository extends GenericRepository[Manager]

case class ManagerRepositoryDynamo(tableName: String)
    extends ManagerRepository {

  override def create(item: Manager): IO[Manager] = {

    val item = ManagerItem.fromModel(item).toMap().asJava
//
//    IO.fromFuture(
//      instance
//        .single(
//          new PutItemRequest()
//            .withTableName(tableName)
//            .withItem(item)
//            .toOp).flat)

//    Source.single(new CreateTableRequest().withTableName(""))

//    Source.single()

  }

  def findByIdTemp(id: String): IO[Manager] = {
    val key = Map("uid" -> new AttributeValue(id)).asJava
    IO.fromFuture(
      instance
        .single(new GetItemRequest().withTableName(tableName).withKey(key).toOp)
        .map { r =>
          val map = r.getItem.asScala.toMap
          ManagerItem.modelFromMap(map)
        }
    )

  }

  override def findById(id: String): IO[Option[Manager]] = {
//    val key = Map("uid" -> new AttributeValue(id)).asJava
//    IO.fromFuture(
//      instance
//        .single(new GetItemRequest().withTableName(tableName).withKey(key).toOp)
//        .map { r =>
//          val i = r.getItem.asScala;
//          if (i.isEmpty)
//            None
//          else
//            Some(ManagerItem.modelFromMap(i))
//        }
//    )
    ???
  }

  override def list(): IO[Seq[Manager]] = ???
}

object ManagerRepositoryDynamo {
  import dynamodb.utils.CreateTableUtil._

  val tableName = "tbl_manager"

  def apply: Future[ManagerRepositoryDynamo] = {
    tableExists(tableName).flatMap { exists: Boolean =>
      val instance = new ManagerRepositoryDynamo(tableName)
      if (exists)
        Future(instance)
      else
        createTable(tableName).map(_ => instance)
    }
  }

}
