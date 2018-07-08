package generators

import java.util.UUID

import model.Entities.Manager
import model.ManagerAlgebra
import org.scalacheck.{Arbitrary, Gen}

import scala.collection.mutable.ListBuffer

object ManagersGen {

  val managerNicks: ListBuffer[String] = ListBuffer.empty

  val managerGen: Gen[Manager] = for {
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr.withFilter(nick => !managerNicks.contains(nick))
  } yield {
    managerNicks += nickname
    Manager(None, name, nickname)
  }
  val managersBatchGen: Gen[List[Manager]] = Gen.listOfN(100, managerGen)

}
