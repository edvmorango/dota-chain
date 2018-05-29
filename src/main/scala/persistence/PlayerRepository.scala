package persistence

import cats.effect.IO
import model.Entities.Player

trait PlayerRepository extends GenericRepository[Player]

case class DynamoDBPlayerRepository(tableName: String)
    extends PlayerRepository {

  def create(item: Player): IO[String] = ???

  def findById(id: String): IO[Option[Player]] = ???

  def list(): IO[Seq[Player]] = ???
}
