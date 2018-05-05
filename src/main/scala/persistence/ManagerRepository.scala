package persistence

import cats.effect.IO
import model.Entities.{Manager, UID}

trait ManagerRepository extends GenericRepository[Manager]


class ManagerRepositoryImpl extends ManagerRepository{

  override def create(alg: Manager): IO[Manager] = {

    IO.async { cb => {
//      cb
      // db access
    }}


  }

  override def findById(alg: UID): IO[Option[Manager]] = ???

  override def list(): IO[Seq[Manager]] = ???

}
