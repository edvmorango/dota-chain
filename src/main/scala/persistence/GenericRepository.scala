package persistence

import cats.effect.IO

trait GenericRepository[A] {

  def create(obj: A): IO[String]

  def findById(id: String): IO[Option[A]]

  def list(): IO[Seq[A]]

}
