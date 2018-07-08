package service

import generators.ManagersGen
import model.Entities.Manager
import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop.forAll
import org.scalatest.{AsyncWordSpec, MustMatchers, WordSpec}
import repository.IMManagerRepository

class ManagerServiceSpec extends WordSpec with MustMatchers {

  import ManagersGen._

  val rep = new IMManagerRepository
  val service = ManagerServiceImpl(rep)

  "ManagerService" should {

    "create a manager" in {

      managersBatchGen
//        .retryUntil(_ => true) Should be side-effect free
      .sample.get
        .foreach(service.create(_).unsafeRunSync().isRight mustBe true)

    }
    "find a manager by id" in {

      service.list().unsafeRunSync().foreach { ops =>
        service.findById(ops.uid.get).unsafeRunSync().isDefined mustBe true
      }

    }

    "find a manager by nickname" in {

      service
        .list()
        .unsafeRunSync()
        .foreach(
          e =>
            service
              .findByNickname(e.nickname)
              .unsafeRunSync()
              .isDefined mustBe true)

    }

    "list all managers" in {

      service.list().unsafeRunSync().nonEmpty mustBe true

    }

    "not a create a manager with a already existing nickname" in {

      service
        .list()
        .unsafeRunSync()
        .map(_.copy(None))
        .foreach(service.create(_).unsafeRunSync().isLeft mustBe true)

    }
  }

}
