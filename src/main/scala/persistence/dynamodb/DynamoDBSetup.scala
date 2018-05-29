package persistence.dynamodb

import cats.effect.IO
import persistence.{
  DynamoDBManagerRepository,
  DynamoDBPlayerRepository,
  DynamoDBTeamRepository
}
import syntax.IOSyntax._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DynamoDBSetup {

  import DynamoDBClient._
  import persistence.dynamodb.utils.CreateTableUtil._

  def setupDatabase(): IO[Unit] = {
    for {
      _ <- createIfNotExists(DynamoDBManagerRepository.tableName)
      _ <- createIfNotExists(DynamoDBPlayerRepository.tableName)
      _ <- createIfNotExists(DynamoDBTeamRepository.tableName)
    } yield ()

  }

  def createIfNotExists(table: String): IO[Unit] = {

    IO {
      tableExists(table)
        .flatMap { exists: Boolean =>
          if (exists)
            Future(())
          else
            createTable(table)
        }
    }
  }.flatIO()

}
