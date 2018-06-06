package repository

import java.util.UUID

import cats.effect.IO
import model.Entities
import model.Entities.Manager
import persistence.ManagerRepository

case class IMManagerRepository() extends ManagerRepository {

  val kv = collection.mutable.Map.empty[String, Manager]

  def getUUID = {
    Thread.sleep(50)
    UUID.randomUUID().toString
  }
  override def findByNickname(nickname: String): IO[Option[Entities.Manager]] =
    IO(kv.values.find(_.nickname == nickname))

  override def create(obj: Entities.Manager): IO[String] = {
    val uuid = getUUID
    for {
      _ <- IO {
        kv.update(uuid, obj.copy(Some(uuid)))
        uuid
      }
    } yield uuid
  }
  override def findById(id: String): IO[Option[Entities.Manager]] =
    IO(kv.get(id))

  override def list(): IO[Seq[Entities.Manager]] = IO(kv.values.toSeq)
}
