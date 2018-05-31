package service

import cats.effect.IO
import model.Entities.Team
import persistence.TeamRepository

trait TeamService extends GenericService[Team] {

  def findByTag(tag: String): IO[Option[Team]]

  def findByPlayer(player: String): IO[Option[Team]]

}

case class TeamServiceImpl(rep: TeamRepository) extends TeamService {

  def create(obj: Team): IO[Either[Throwable, Team]] = {

    val pipe = for {
      tag <- rep.findByTag(obj.tag)
      _ <- tag match {
        case None => IO.pure(Unit)
        case _    => IO.raiseError(new Exception("The team tag already exists."))
      }
      ins <- rep.create(obj)
      team <- rep.findById(ins)
    } yield team.get

    pipe.attempt

  }

  def findById(id: String): IO[Option[Team]] = rep.findById(id)

  def list(): IO[Seq[Team]] = rep.list()

  def findByTag(tag: String): IO[Option[Team]] = rep.findByTag(tag)

  def findByPlayer(player: String): IO[Option[Team]] = rep.findByPlayer(player)

}
