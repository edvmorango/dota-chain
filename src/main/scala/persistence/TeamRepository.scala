package persistence

import cats.effect.IO
import model.Entities.Team

trait TeamRepository extends GenericRepository[Team]

case class DynamoDBTeamRepository(tableName: String) extends TeamRepository {

  def create(item: Team): IO[String] = ???

  def findById(id: String): IO[Option[Team]] = ???

  def list(): IO[Seq[Team]] = ???

}
