package endpoints

import cats.effect._
import service.ManagerService
import model.Entities.Manager
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.dsl.Http4sDsl
import cats.effect.Effect
import cats.implicits._

case class ManagerEndpoint(service: ManagerService) {

  implicit val decoder = jsonOf[IO, Manager]

  private def create(): HttpService[IO] = HttpService[IO] {
    case req @ POST -> Root / "manager" =>
      for {
        i <- req.as[Manager]
        o <- service.create(i)
        r <- o.fold(t => BadRequest(t.getMessage), obj => Ok(obj.asJson))
      } yield r

  }

  private def findById(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "manager" / id =>
      service.findById(id).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The manager was not found")
      }
  }

  private def list(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "manager" =>
      service.list().flatMap(v => Ok(v.asJson))
  }

  private def findByNickname(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "manager" / "nickname" / nick =>
      service.findByNickname(nick).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The manager was not found")
      }

  }

  def endpoints(): HttpService[IO] =
    create() <+> findById() <+> list() <+> findByNickname()

}
