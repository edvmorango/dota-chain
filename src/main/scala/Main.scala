import cats.effect.IO
import fs2.{Stream, StreamApp}
import fs2.StreamApp.ExitCode
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global


object Main extends StreamApp[IO] {

  val root = HttpService[IO] {
    case GET -> Root / "id" =>
      Ok("Index")
  }


  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {

    BlazeBuilder[IO].bindHttp(8000, "localhost")
      .mountService(root, "/")
      .serve

  }




}
