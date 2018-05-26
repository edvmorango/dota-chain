package persistence.dynamodb

import cats.effect.IO
import persistence.ManagerRepositoryDynamo
import syntax.IOSyntax._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DynamoDBSetup {

  import DynamoDBClient._
  import persistence.dynamodb.utils.CreateTableUtil._

  def setupDatabase(): IO[Unit] = {
    for {
      _ <- createIfNotExists(ManagerRepositoryDynamo.tableName)
    } yield ()

  }

  def createIfNotExists(table: String): IO[Unit] = {
    tableExists(table)
      .flatMap { exists: Boolean =>
        if (exists)
          Future(())
        else
          createTable(table)
      }
      .toIO()
  }

}
