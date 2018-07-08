package integration

import cats.effect.IO
import endpoints.ManagerEndpoint
import generators.ManagersGen
import model.Entities.Manager
import org.http4s.{Request, Uri}
import org.http4s.dsl.Http4sDsl
import org.scalatest.{MustMatchers, WordSpec}
import repository.IMManagerRepository
import service.ManagerServiceImpl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import org.scalatest._

class ManagerIntegrationSpec
    extends WordSpec
    with MustMatchers
    with Http4sDsl[IO] {

  import ManagersGen._

  val managerRepository = IMManagerRepository()
  val managerService = ManagerServiceImpl(managerRepository)

  val endpoint = ManagerEndpoint(managerService).endpoints()

  "Manager endpoint" should {

    "create user" in {

      managersBatchGen.sample.get.foreach { e: Manager =>
        val request = Request[IO](POST, uri = Uri.uri("api/v1/manager"))
          .withBody(e.asJson)

        for {
          req <- request
          res <- endpoint.run(req).getOrElse(fail(s"create user failing."))
        } yield res.status mustBe Ok

      }

    }

    "list users" in {
      val request = Request[IO](GET, uri = Uri.uri("api/v1/manager"))
      for {
        res <- endpoint.run(request).getOrElse(fail(s"list user failing."))
      } yield res.status mustBe Ok

    }
//
//    "find user by id" in {
//
//      managersBatchGen.sample.get.foreach { e: Manager =>
//        val request = Request[IO](POST, uri = Uri.uri("api/v1/manager"))
//          .withBody(e.asJson)
//
//        for {
//          req <- request
//          res <- endpoint
//            .run(req)
//            .getOrElse(fail("create user to find failing."))
//          xd <- res.`
//          found <- endpoint.run(
//            Request[IO](GET,
//                        uri = Uri.uri(s"api/v1/manager/${created.uid.get}")))
//        } yield found.status mustBe Ok
//
//      }
//
//    }
  }
}
