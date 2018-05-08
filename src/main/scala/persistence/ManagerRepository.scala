package persistence

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.effect.IO
import com.amazonaws.services.dynamodbv2.model.{CreateTableRequest, PutItemRequest}
import model.Entities.{Manager, UID}
import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._



trait ManagerRepository extends GenericRepository[Manager]

class ManagerRepositoryImpl extends ManagerRepository{

  override def create(item: Manager): IO[Manager] = {

//    IO.fromFuture()



    val req = new PutItemRequest("tbl_manager")


    DynamoDBClient.client
      .single().map(x => x.cr)


  }

  override def findById(id: UID): IO[Option[Manager]] = ???

  override def list(): IO[Seq[Manager]] = ???
}
