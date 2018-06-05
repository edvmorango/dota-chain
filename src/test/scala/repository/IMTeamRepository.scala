package repository

import java.util.UUID

import cats.effect.IO
import model.Entities
import model.Entities.Team
import persistence.TeamRepository

class IMTeamRepository extends TeamRepository {

  val kv = collection.mutable.Map.empty[String, Team]

  def getUUID = {
    Thread.sleep(50)
    UUID.randomUUID().toString
  }

  def findById(id: String): IO[Option[Entities.Team]] = IO(kv.get(id))

  def findByTag(tag: String): IO[Option[Entities.Team]] =
    IO(kv.values.find(_.tag == tag))

  def create(obj: Entities.Team): IO[String] = {
    val uuid = getUUID

    for {
      _ <- IO {
        kv.update(uuid, obj.copy(Some(uuid)))
        uuid
      }
    } yield uuid

  }
  def list(): IO[Seq[Entities.Team]] = IO(kv.values.toSeq)

  def findByPlayer(player: String): IO[Option[Entities.Team]] =
    IO(kv.values.find(e => e.players.exists(_.nickname == player)))

}
