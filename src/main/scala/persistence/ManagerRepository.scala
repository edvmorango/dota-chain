package persistence

import java.util.UUID

import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient
import akka.stream.scaladsl.Source
import cats.effect.IO
import model.Entities.{Manager, Player, UID}
import com.amazonaws.services.dynamodbv2.model._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import persistence.dynamodb.DynamoDBClient

import scala.xml.Attribute

trait ManagerRepository extends GenericRepository[Manager]

class ManagerRepositoryImpl extends ManagerRepository {

  override def create(item: Manager): IO[Manager] = {

//    IO.fromFuture()

//    Source.single(new CreateTableRequest().withTableName(""))

//    Source.single()

    ???

  }

  override def findById(id: UID): IO[Option[Manager]] = ???

  override def list(): IO[Seq[Manager]] = ???
}

import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._

object ManagerRepositoryImpl extends App {
  import persistence.dynamodb.utils.CreateTableRequestUtil._
  import DynamoDBClient._

  val request =
    new CreateTableRequest()
      .withTableName("tbl_manager")
      .withKeySchema(H("uid"), R("rid"))
      .withAttributeDefinitions(S("uid"), S("rid"))
      .withProvisionedThroughput(throughput)
      .toOp

  val result = instance.single(request)

  private val res = Await.result(result, 10000 millis)

  println(res.toString)
}
