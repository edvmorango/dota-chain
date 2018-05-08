package persistence

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient

object DynamoDBClient {

  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()
  private val settings = DynamoSettings.apply(system);

  val client = DynamoClient(settings)

}
