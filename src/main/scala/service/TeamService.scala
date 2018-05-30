package service

import cats.effect.IO
import model.Entities.Team
import persistence.TeamRepository

trait TeamService extends GenericService[Team] {

  def findByTag(tag: String): IO[Option[Team]]

  def findByPlayer(player: String): IO[Option[Team]]

}

case class TeamServiceImpl(rep: TeamRepository) extends TeamService {

  def create(obj: Team): IO[Either[Throwable, Team]] = ???

  def findById(id: String): IO[Option[Team]] = rep.findById(id)

  def list(): IO[Seq[Team]] = rep.list()

  def findByTag(tag: String): IO[Option[Team]] = rep.findByTag(tag)

  def findByPlayer(player: String): IO[Option[Team]] = rep.findByPlayer(player)

}
