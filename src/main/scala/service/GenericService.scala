package service

import cats.effect.IO

trait GenericService[A] {

  def create(obj: A): IO[Either[Throwable, A]]

  def findById(id: String): IO[Option[A]]

  def list(): IO[Seq[A]]

}
