package service

import cats.effect.IO
import model.Entities.Manager
import persistence.ManagerRepository
import cats.implicits._

trait ManagerService extends GenericService[Manager] {

  def findByNickname(nickname: String): IO[Option[Manager]]

}

case class ManagerServiceImpl(rep: ManagerRepository) extends ManagerService {

  def create(obj: Manager): IO[Either[Throwable, Manager]] = {
    val exp = for {
      found <- findByNickname(obj.nickname)
      _ <- found match {
        case None => IO.pure(Unit)
        case _ =>
          IO.raiseError(new Exception("The manager nickname already exists."))
      }
      id <- rep.create(obj)
      ins <- rep.findById(id)
    } yield {
      ins.get
    }

    exp.attempt

  }

  def findById(id: String): IO[Option[Manager]] = rep.findById(id)

  def findByNickname(nickname: String): IO[Option[Manager]] =
    rep.findByNickname(nickname)

  def list(): IO[Seq[Manager]] = rep.list()

}
