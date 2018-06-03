package generators

import model.Entities.Player
import org.scalacheck.Gen

object PlayersGen {

  val player: Gen[Player] = for {
    name <- Gen.asciiStr
    nickname <- Gen.asciiStr
  } yield Player(None, name, nickname)

  val playersBatch: Gen[List[Player]] = Gen.listOf(player)

}
