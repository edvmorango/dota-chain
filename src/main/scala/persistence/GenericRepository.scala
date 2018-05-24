package persistence

import cats.effect.IO

trait GenericRepository[A] {

  def create(item: A): IO[A]

  def findById(id: String): IO[Option[A]]

  def list(): IO[Seq[A]]

}
