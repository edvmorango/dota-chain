package persistence

import cats.effect.IO
import model.Entities.UID


trait GenericRepository[A] {

  def create(alg: A): IO[A]

  def findById(alg: UID): IO[Option[A]]

  def list(): IO[Seq[A]]

}
