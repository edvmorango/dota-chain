package service

import cats.effect.IO
import model.Entities.Manager
import persistence.ManagerRepository

trait ManagerService extends GenericService[Manager] {}

case class ManagerServiceImpl(rep: ManagerRepository) extends ManagerService {

  def create(obj: Manager): IO[Either[Throwable, Manager]] = {

    val exp = for {
      id <- rep.create(obj)
      ins <- rep.findById(id)
    } yield {
      ins.get
    }

    exp.attempt

  }

  def findById(id: String): IO[Option[Manager]] = rep.findById(id)

  def list(): IO[Seq[Manager]] = rep.list()

}
