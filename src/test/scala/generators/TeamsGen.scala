package generators

import model.Entities.{Player, Team}
import org.scalacheck.Gen

import scala.collection.mutable.ListBuffer

object TeamsGen {
  import PlayersGen._

  private val teamTags: ListBuffer[String] = ListBuffer.empty

  val teamGen: Gen[Team] = for {
    name <- Gen.asciiStr
    tag <- Gen.asciiStr
      .map(_.slice(0, 5))
      .withFilter(nick => !teamTags.contains(nick))
    players <- Gen.listOfN(5, playerGen)
  } yield {
    teamTags += tag
    Team(None, name, tag, players.toSet)
  }

  val teamsBatchGen: Gen[List[Team]] = Gen.listOfN(100, teamGen)

}
