package generators

import model.Entities.Manager
import model.ManagerAlgebra
import org.scalacheck.Gen

trait ManagersGen {

  val manager: Gen[Manager] = for {
    uid <- Gen.option(Gen.uuid.toString)
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr
  } yield Manager(uid, name, nickname)

  val managersBatch: Gen[List[Manager]] = Gen.listOf(manager)

}
