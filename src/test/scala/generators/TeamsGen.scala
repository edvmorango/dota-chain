package generators

import model.Entities.{Player, Team}
import org.scalacheck.Gen

object TeamsGen {

  val team: Gen[Team] = for {
    name <- Gen.asciiStr
  } yield Team(None, name, name.slice(0, 3), Set.empty[Player])

  val teamsBatch: Gen[List[Team]] = Gen.listOf(team)

}
