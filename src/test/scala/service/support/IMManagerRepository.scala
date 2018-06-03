package service.support

import java.util.UUID

import cats.effect.IO
import model.Entities
import model.Entities.Manager
import persistence.ManagerRepository

class IMManagerRepository extends ManagerRepository {

  val kv = collection.mutable.Map.empty[String, Manager]

  override def findByNickname(nickname: String): IO[Option[Entities.Manager]] =
    IO(kv.values.find(_.nickname == nickname))

  override def create(obj: Entities.Manager): IO[String] =
    for {
      _ <- IO { kv.update(obj.uid.get, obj) }
    } yield obj.uid.get

  override def findById(id: String): IO[Option[Entities.Manager]] =
    IO(kv.get(id))

  override def list(): IO[Seq[Entities.Manager]] = IO(kv.values.toList)
}
