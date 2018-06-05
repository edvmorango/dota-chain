package generators

import model.Entities.Player
import org.scalacheck.Gen

import scala.collection.mutable.ListBuffer

trait PlayersGen {

  private val playerNicks: ListBuffer[String] = ListBuffer.empty

  val playerGen: Gen[Player] = for {
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr.withFilter(nick => !playerNicks.contains(nick))
  } yield {

    Player(None, name, nickname)
  }
  val playersBatchGen: Gen[List[Player]] = Gen.listOf(playerGen)

}
