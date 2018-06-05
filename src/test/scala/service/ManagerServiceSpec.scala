package service

import generators.ManagersGen
import model.Entities.Manager
import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop.forAll
import org.scalatest.{AsyncWordSpec, MustMatchers, WordSpec}
import repository.IMManagerRepository

class ManagerServiceSpec extends WordSpec with MustMatchers with ManagersGen {

  val rep = new IMManagerRepository
  val service = ManagerServiceImpl(rep)

  "ManagerService" should {

    "create a manager" in {

      managersBatchGen.sample.get
        .foreach(service.create(_).unsafeRunSync().isRight mustBe true)

    }
    "find a manager by id" in {

      service.list().unsafeRunSync().foreach { ops =>
        service.findById(ops.uid.get).unsafeRunSync().isDefined mustBe true
      }

    }

    "find a manager by nickname" in {

      managerNicks.foreach(
        service.findByNickname(_).unsafeRunSync().isDefined mustBe true
      )

    }

    "list all managers" in {

      service.list().unsafeRunSync().size > 0 mustBe true

    }

    "not a create a manager with a already existing nickname" in {

      managerNicks
        .map(Manager(None, "", _))
        .foreach(
          service.create(_).unsafeRunSync().isLeft mustBe true
        )

    }

  }

}
