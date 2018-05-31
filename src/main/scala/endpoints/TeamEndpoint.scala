package endpoints

import cats.effect.IO
import model.Entities.Team
import org.http4s.HttpService
import org.http4s.circe.jsonOf
import org.http4s.dsl.io.{->, /, BadRequest, GET, NotFound, Ok, POST, Root}
import service.TeamService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.dsl.Http4sDsl
import cats.implicits._

case class TeamEndpoint(service: TeamService) {

  implicit val decoder = jsonOf[IO, Team]

  private def create(): HttpService[IO] = HttpService[IO] {
    case req @ POST -> Root / "team" =>
      for {
        i <- req.as[Team]
        o <- service.create(i)
        r <- o.fold(t => BadRequest(t.getMessage), obj => Ok(obj.asJson))
      } yield r

  }

  private def findById(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "team" / id =>
      service.findById(id).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The Team was not found")
      }
  }

  private def list(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "team" =>
      service.list().flatMap(v => Ok(v.asJson))
  }

  private def findByTag(): HttpService[IO] = HttpService[IO] {
    case GET -> Root / "team" / "tag" / tag =>
      service.findByTag(tag).flatMap {
        case Some(r) => Ok(r.asJson)
        case None    => NotFound("The Team was not found")
      }

  }

//  private def findByPlayerId(): HttpService[IO] = HttpService[IO] {
//    case GET -> Root / "team" / "tag" / nick =>
//      service.findByTag(nick).flatMap {
//        case Some(r) => Ok(r.asJson)
//        case None    => NotFound("The Team was not found")
//      }
//
//  }
//
//  private def findByPlayerNickname(): HttpService[IO] = HttpService[IO] {
//    case GET -> Root / "team" / "player" / nick =>
//      service.findByTag(nick).flatMap {
//        case Some(r) => Ok(r.asJson)
//        case None    => NotFound("The Team was not found")
//      }

//  }

  def endpoints(): HttpService[IO] =
    create() <+> findById() <+> list() <+> findByTag() //<+> findByPlayerId() <+> findByPlayerNickname()

}
