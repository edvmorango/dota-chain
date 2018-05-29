package service

import cats.effect.IO
import model.Entities
import model.Entities.Player
import persistence.PlayerRepository

trait PlayerService extends GenericService[Player]

case class PlayerServiceImpl(rep: PlayerRepository) extends PlayerService {

  def create(obj: Player): IO[Either[Throwable, Player]] = ???

  def findById(id: String): IO[Option[Player]] = ???

  def list(): IO[Seq[Player]] = ???

}
