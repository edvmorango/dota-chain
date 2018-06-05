package generators

import model.Entities.{Player, Team}
import org.scalacheck.Gen

import scala.collection.mutable.ListBuffer

trait TeamsGen {

  private val teamTags: ListBuffer[String] = ListBuffer.empty

  val team: Gen[Team] = for {
    name <- Gen.asciiStr
    tag <- Gen.asciiStr
      .map(_.slice(0, 5))
      .withFilter(nick => !teamTags.contains(nick))
  } yield {
    teamTags += tag
    Team(None, name, tag, Set.empty[Player])
  }
  val teamsBatch: Gen[List[Team]] = Gen.listOf(team)

}
