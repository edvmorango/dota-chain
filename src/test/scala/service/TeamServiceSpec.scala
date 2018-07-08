package service

import generators.TeamsGen
import model.Entities.Team
import org.scalatest.{MustMatchers, WordSpec}
import repository.IMTeamRepository

class TeamServiceSpec extends WordSpec with MustMatchers {
  import TeamsGen._

  val rep = new IMTeamRepository
  val service = TeamServiceImpl(rep)

  "TeamService" should {

    "create a team" in {

      teamsBatchGen
//        .retryUntil(_ => true)
      .sample.get
        .foreach(service.create(_).unsafeRunSync().isRight mustBe true)

    }

    "find a team by id" in {

      service.list().unsafeRunSync().foreach { ops =>
        service.findById(ops.uid.get).unsafeRunSync().isDefined mustBe true
      }

    }

    "find a team by tag" in {

      service
        .list()
        .unsafeRunSync()
        .foreach(
          e =>
            service
              .findByTag(e.tag)
              .unsafeRunSync()
              .isDefined mustBe true)
    }

    "list all teams" in {

      service.list().unsafeRunSync().nonEmpty mustBe true

    }

    "find a team by player nickname" in {

      service
        .list()
        .unsafeRunSync()
        .foreach(
          e =>
            service
              .findByPlayer(e.players.head.nickname)
              .unsafeRunSync()
              .isDefined mustBe true)

    }

    "not a create a team with a already existing tag" in {

      service
        .list()
        .unsafeRunSync()
        .map(_.copy(None))
        .foreach(
          service.create(_).unsafeRunSync().isLeft mustBe true
        )

    }

  }

}
