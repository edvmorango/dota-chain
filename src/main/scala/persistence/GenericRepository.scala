package persistence

import cats.effect.IO
import model.Entities.UID


trait GenericRepository[A] {

  def create(item: A): IO[A]

  def findById(id: UID): IO[Option[A]]

  def list(): IO[Seq[A]]

}
