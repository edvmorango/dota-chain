package generators

import model.Entities.Player
import org.scalacheck.Gen

import scala.collection.mutable.ListBuffer

object PlayersGen {

  private val playerNicks: ListBuffer[String] = ListBuffer.empty

  val playerGen: Gen[Player] = for {
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr.withFilter(nick => !playerNicks.contains(nick))
  } yield {
    playerNicks += nickname
    Player(None, name, nickname)
  }
  val playersBatchGen: Gen[List[Player]] = Gen.listOfN(100, playerGen)

}
