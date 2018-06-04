package repository

import java.util.UUID

import cats.effect.IO
import model.Entities
import model.Entities.Player
import persistence.PlayerRepository

class IMPlayerRepository extends PlayerRepository {

  val kv = collection.mutable.Map.empty[String, Player]

  def getUUID = {
    Thread.sleep(50)
    UUID.randomUUID().toString
  }
  override def findByNickname(nickname: String): IO[Option[Entities.Player]] =
    IO(kv.values.find(_.nickname == nickname))

  override def create(obj: Entities.Player): IO[String] = {
    val uuid = getUUID
    for {
      _ <- IO {
        kv.update(uuid, obj.copy(Some(uuid)))
        uuid
      }
    } yield uuid
  }
  override def findById(id: String): IO[Option[Entities.Player]] =
    IO(kv.get(id))

  override def list(): IO[Seq[Entities.Player]] = IO(kv.values.toList)

}
