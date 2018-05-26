package syntax

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}

object IOSyntax {

  implicit class FutureToIO[A](future: Future[A]) {

    def toIO()(implicit parser: ExecutionContext): IO[A] =
      IO fromFuture
        IO { future }

  }

}
