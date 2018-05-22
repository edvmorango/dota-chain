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

object ManagerRepositoryImpl {
  import dynamodb.utils.CreateTableUtil._

  val TABLE = "tbl_manager"

  def apply: Future[ManagerRepositoryImpl] = {
    tableExists(TABLE).flatMap { b: Boolean =>
      val instance = new ManagerRepositoryImpl
      if (b)
        Future(instance)
      else
        createTable(TABLE).map(_ => instance)
    }
  }

}
