package service

import cats.effect.IO
import model.Entities
import model.Entities.Player
import persistence.PlayerRepository

trait PlayerService extends GenericService[Player] {

  def findByNickname(nickname: String): IO[Option[Player]]

}

case class PlayerServiceImpl(rep: PlayerRepository) extends PlayerService {

  def create(obj: Player): IO[Either[Throwable, Player]] = {

    val pipe = for {
      found <- rep.findByNickname(obj.nickname)
      _ <- found match {
        case None => IO.pure(Unit)
        case _ =>
          IO.raiseError(new Exception("The player nickname already exists."))
      }
      id <- rep.create(obj)
      ins <- findById(id)
    } yield ins.get

    pipe.attempt

  }

  def findById(id: String): IO[Option[Player]] = rep.findById(id)

  def list(): IO[Seq[Player]] = rep.list()

  def findByNickname(nickname: String): IO[Option[Player]] =
    rep.findByNickname(nickname)
}
