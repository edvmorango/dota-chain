package persistence

import java.util.UUID

import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits.{
  CreateTable,
  ListTables
}
import cats.effect.IO
import model.Entities.{Manager, Player, UID}
import akka.stream.scaladsl.Source
import cats.free.Free
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.gu.scanamo._
import com.gu.scanamo.error.DynamoReadError
import com.gu.scanamo.ops.{ScanamoOps, ScanamoOpsA}
import com.gu.scanamo.syntax._
import persistence.dynamodb.DynamoDBClient

trait ManagerRepository extends GenericRepository[Manager]

class ManagerRepositoryImpl extends ManagerRepository {

  override def create(item: Manager): IO[Manager] = {

//    IO.fromFuture()

//    Source.single(new CreateTableRequest().withTableName(""))

    Source.single()

    ???

  }

  override def findById(id: UID): IO[Option[Manager]] = ???

  override def list(): IO[Seq[Manager]] = ???
}

object ManagerRepositoryImpl extends App {

  import com.gu.scanamo._
  import com.gu.scanamo.syntax._

  val client = DynamoDBClient.instance

  case class PlayerItem(uid: String, name: String, nickname: String)

  val playerTable = Table[PlayerItem]("tbl_playeritem")

  val uuid: String = UUID.randomUUID().toString

  val put = playerTable.put {
    PlayerItem(uuid, "Danil Ishutin", "dendi")
  }

  val fetchDendi = playerTable.get('uid -> uuid)

  val pipe: ScanamoOps[PlayerItem] = for {
    _ <- put
    dondo <- fetchDendi
    dondo2 <- dondo.get
  } yield dondo2

  val res = ScanamoAlpakka.exec(client)(pipe)

  private val supaMida: PlayerItem = Await.result(res, 10000 millis)

  println(supaMida)

}
