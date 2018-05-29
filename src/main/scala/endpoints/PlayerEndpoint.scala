package endpoints

import cats.effect._
import model.Entities.Player
import org.http4s.HttpService
import service.PlayerService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.dsl.Http4sDsl
import cats.implicits._

case class PlayerEndpoint(service: PlayerService) {

  implicit val decoder = jsonOf[IO, Player]

  private def create(): HttpService[IO] = HttpService[IO] {
    case req @ POST -> Root / "player" =>
      for {
        i <- req.as[Player]
        o <- service.create(i)
        r <- o.fold(t => BadRequest(t.getMessage), obj => Ok(obj.asJson))
      } yield r

  }

  private def findById(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "player" / id =>
      service.findById(id).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The player was not found")
      }
  }

  private def list(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "player" =>
      service.list().flatMap(v => Ok(v.asJson))
  }

  private def findByNickname(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "player" / "nickname" / nick =>
      service.findByNickname(nick).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The player was not found")
      }

  }

  def endpoints(): HttpService[IO] =
    create() <+> findById() <+> list() <+> findByNickname()

}
