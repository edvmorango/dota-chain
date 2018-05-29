package service

import cats.effect.IO
import model.Entities.Team

trait TeamService extends GenericService[Team]

case class TeamServiceImpl() extends TeamService {

  def create(obj: Team): IO[Either[Throwable, Team]] = ???

  def findById(id: String): IO[Option[Team]] = ???

  def list(): IO[Seq[Team]] = ???
}
