package persistence

import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits.{
  CreateTable,
  ListTables
}

import scala.collection.JavaConverters._
import cats.effect.IO
import model.Entities.{Manager, UID}
import akka.stream.scaladsl.Source
import com.amazonaws.services.dynamodbv2.model.{
  CreateTableRequest,
  ListTablesRequest
}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait ManagerRepository extends GenericRepository[Manager]

class ManagerRepositoryImpl extends ManagerRepository {

  override def create(item: Manager): IO[Manager] = {

//    IO.fromFuture()

//    Source.single(new CreateTableRequest().withTableName(""))

    Source.single()

    ???

  }

  override def findById(id: UID): IO[Option[Manager]] = ???

  override def list(): IO[Seq[Manager]] = ???
}

object ManagerRepositoryImpl extends App {

  def apply: ManagerRepositoryImpl = {

    val createTable = DynamoDBClient.instance.single(
      new CreateTableRequest().withTableName("tbl_manager").toOp)

    val pqp = Await
      .result(createTable, 50000 millis)

    println(pqp.getTableDescription)
    println("Depois")
    new ManagerRepositoryImpl()

  }

  apply

}
