package generators

import java.util.UUID

import model.Entities.Manager
import model.ManagerAlgebra
import org.scalacheck.{Arbitrary, Gen}

import scala.collection.mutable.ListBuffer

trait ManagersGen {

  val nicks: ListBuffer[String] = ListBuffer.empty

  val managerGen: Gen[Manager] = for {
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr.withFilter(nick => !nicks.exists(e => e == nick))
  } yield {
    nicks += nickname
    Manager(None, name, nickname)
  }
  val managersBatchGen: Gen[List[Manager]] = Gen.listOf(managerGen)

}
