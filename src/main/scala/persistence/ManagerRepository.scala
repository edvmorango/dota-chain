package persistence

import akka.stream.scaladsl.Source
import cats.effect.IO
import model.Entities.{Manager, Player, UID}
import com.amazonaws.services.dynamodbv2.model._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ManagerRepository extends GenericRepository[Manager]

case class ManagerRepositoryDynamo(tableName: String)
    extends ManagerRepository {

  override def create(item: Manager): IO[Manager] = {

//    IO.fromFuture(
//      Source.single(new PutItemRequest().withTableName(tableName).withItem()
//    )

//    Source.single(new CreateTableRequest().withTableName(""))

//    Source.single()

    ???

  }

  override def findById(id: UID): IO[Option[Manager]] = ???

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
