import cats.effect.IO
import endpoints.{ManagerEndpoint, PlayerEndpoint, TeamEndpoint}
import fs2.{Stream, StreamApp}
import fs2.StreamApp.ExitCode
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.server.blaze._
import persistence.{
  DynamoDBManagerRepository,
  DynamoDBPlayerRepository,
  DynamoDBTeamRepository
}
import persistence.dynamodb.DynamoDBSetup
import service.{ManagerServiceImpl, PlayerServiceImpl, TeamServiceImpl}

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends StreamApp[IO] {

  val root = HttpService[IO] {
    case GET -> Root / "id" =>
      Ok("Id")
    case GET -> Root =>
      Ok("Index")
  }

  override def stream(args: List[String],
                      requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {

    val managerEndpoints = ManagerEndpoint(
      ManagerServiceImpl(DynamoDBManagerRepository.apply)).endpoints()

    val playerEndpoints = PlayerEndpoint(
      PlayerServiceImpl(DynamoDBPlayerRepository.apply)).endpoints()

    val teamEndpoints =
      TeamEndpoint(TeamServiceImpl(DynamoDBTeamRepository.apply)).endpoints()

    val api = "/api/v1"

    for {
      _ <- Stream.eval(DynamoDBSetup.setupDatabase()) // Should decouple this
      app <- BlazeBuilder[IO]
        .bindHttp(8080, "localhost")
        .mountService(managerEndpoints, prefix = api)
        .mountService(playerEndpoints, prefix = api)
        .mountService(teamEndpoints, prefix = api)
        .serve
    } yield app

  }

}
