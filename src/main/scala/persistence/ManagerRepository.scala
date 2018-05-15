package persistence

import java.util.UUID

import cats.effect.IO
import model.Entities.{Manager, Player, UID}
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import io.atlassian.aws.dynamodb.{Column, Table, TableDefinition}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import persistence.dynamodb.DynamoDBClient

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

object ManagerRepositoryImpl extends App {

  import io.atlassian.aws.s3.S3Client
  val defaultClient = S3Client.withEndpoint("http://localhost:8000")

  private val key = Column[String]("key")
  private val hash = Column[String]("rangeKey")
  private val name = Column[String]("name")

  TableDefinition.from("tbl_manager", key, hash, name)

}
