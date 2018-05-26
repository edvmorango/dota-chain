package syntax

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}

object IOSyntax {

  implicit class IOFutToIO[A](iof: IO[Future[A]]) {

    def flatIO()(implicit parser: ExecutionContext): IO[A] =
      IO fromFuture iof

  }

}
