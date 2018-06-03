package service

import generators.ManagersGen
import model.Entities.Manager
import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop.forAll
import org.scalatest.{AsyncWordSpec, MustMatchers, WordSpec}
import service.support.IMManagerRepository

class ManagerServiceSpec extends WordSpec with MustMatchers with ManagersGen {

  val rep = new IMManagerRepository
  val service = ManagerServiceImpl(rep)

  "ManagerService" should {

    "create a manager" in {

      Prop
        .forAll(manager) { m: Manager =>
          val value = service.create(m)
          val result = value.unsafeRunSync()

          println(result.fold(_.getMessage, _.uid))

          result.isRight

//          (result.isRight mustBe true)

        }
        .check()

    }

    "find a manager by id" in {
      1 mustBe 1
    }

    "find a manager by nickname" in {
      1 mustBe 1
    }

    "list all managers" in {
      1 mustBe 1
    }

    "not a create a manager with a already existing nickname" in {
      1 mustBe 1
    }

  }

}
