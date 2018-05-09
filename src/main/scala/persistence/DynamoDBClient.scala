package persistence

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl
import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient
object DynamoDBClient {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  lazy val instance = {
    System.setProperty("aws.accessKeyId", "anAccessKeyId")
    System.setProperty("aws.secretKey", "aSecretKey")
    val settings = DynamoSettings(system);
    DynamoClient(settings)
  }

}
