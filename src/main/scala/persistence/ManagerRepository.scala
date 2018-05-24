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
import fs2.Pipe.Stepper.Await

import scala.concurrent
import scala.concurrent.duration.DurationLong

trait ManagerRepository extends GenericRepository[Manager]

case class ManagerRepositoryDynamo(tableName: String)
    extends ManagerRepository {

  override def create(obj: Manager): IO[Manager] = {

    val item = ManagerItem.fromModel(obj)
    val itemMap = item.toMap().asJava
    IO.fromFuture(IO {
        instance.single(
          new PutItemRequest()
            .withTableName(tableName)
            .withItem(itemMap)
            .toOp)
      })
      .flatMap(_ => findByIdTemp(item.uid))

//    Source.single(new CreateTableRequest().withTableName(""))

//    Source.single()

  }

  def findByIdTemp(id: String): IO[Manager] = {
    val key = Map("uid" -> new AttributeValue().withS(id),
                  "rid" -> new AttributeValue().withS("rangeKey")).asJava
    IO.fromFuture(
      IO pure
        instance
          .single(
            new GetItemRequest().withTableName(tableName).withKey(key).toOp)
          .map { r =>
            val map = r.getItem.asScala.toMap
            ManagerItem.modelFromMap(map)
          })
  }

  def findById(id: String): IO[Option[Manager]] = {
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

  def apply: ManagerRepositoryDynamo = {

    concurrent.Await.result(
      tableExists(tableName)
        .flatMap { exists: Boolean =>
          val instance = new ManagerRepositoryDynamo(tableName)
          if (exists)
            Future(instance)
          else
            createTable(tableName).map(_ => instance)
        },
      3000 millis
    )
  }

}

object RunIt extends App {

  val rep: ManagerRepository = ManagerRepositoryDynamo.apply

  val manager = Manager(None, "Igor", "Caff")

  val comp: IO[Unit] =
    for {
      i <- rep.create(manager)
    } yield {
      println(i.uid)
      println(i.name)
      println(i.nickname)
    }

  comp.unsafeRunSync()

  println("Finished")
}
