package persistence.dynamodb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.{
  AmazonDynamoDBAsyncClientBuilder,
  AmazonDynamoDBClientBuilder
}

object DynamoDBClient {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  lazy val instance = {
    System.setProperty("aws.accessKeyId", "anAccessKeyId")
    System.setProperty("aws.secretKey", "aSecretKey")
    val settings = DynamoSettings(system);
    DynamoClient(settings)

//    val conf = new EndpointConfiguration("http://localhost:8000", "us-east-1")
//    AmazonDynamoDBClientBuilder
//      .standard()
//      .withEndpointConfiguration(conf)
//      .build()

  }

}
