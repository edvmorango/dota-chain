package service

import generators.PlayersGen
import model.Entities.Player
import org.scalacheck.Prop
import org.scalatest.{MustMatchers, WordSpec}
import repository.IMPlayerRepository

class PlayerServiceSpec extends WordSpec with MustMatchers with PlayersGen {

  val rep = new IMPlayerRepository
  val service = PlayerServiceImpl(rep)

  "PlayerService" should {

    "create a player" in {

      playersBatchGen.sample.get
        .foreach(service.create(_).unsafeRunSync().isRight mustBe true)

    }

    "find a player by id" in {

      service.list().unsafeRunSync().foreach { ops =>
        service.findById(ops.uid.get).unsafeRunSync().isDefined mustBe true
      }

    }

    "find a player by nickname" in {

      playerNicks.foreach(
        service.findByNickname(_).unsafeRunSync().isDefined mustBe true
      )

    }

    "list all players" in {

      service.list().unsafeRunSync().size > 0 mustBe true

    }

    "not a create a player with a already existing nickname" in {

      playerNicks
        .map(Player(None, "", _))
        .foreach(
          service.create(_).unsafeRunSync().isLeft mustBe true
        )

    }

  }

}
