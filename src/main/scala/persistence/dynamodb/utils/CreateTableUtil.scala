package persistence.dynamodb.utils

import cats.data.EitherT
import com.amazonaws.services.dynamodbv2.model._
import persistence.dynamodb.DynamoDBClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object CreateTableUtil {

  import DynamoDBClient._
  import KeyType._
  import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
  import scala.collection.JavaConverters._

  def H(name: String): KeySchemaElement =
    new KeySchemaElement().withAttributeName(name).withKeyType(HASH)

  def R(name: String): KeySchemaElement =
    new KeySchemaElement().withAttributeName(name).withKeyType(RANGE)

  def S(name: String): AttributeDefinition =
    new AttributeDefinition()
      .withAttributeName(name)
      .withAttributeType(ScalarAttributeType.S)

  def B(name: String): AttributeDefinition =
    new AttributeDefinition()
      .withAttributeName(name)
      .withAttributeType(ScalarAttributeType.B)

  def N(name: String): AttributeDefinition =
    new AttributeDefinition()
      .withAttributeName(name)
      .withAttributeType(ScalarAttributeType.N)

  def tableExists(tableName: String): Future[Boolean] =
    listTables().flatMap(t => Future(t.contains(tableName)))

  def listTables(): Future[Seq[String]] =
    instance
      .single(new ListTablesRequest().toOp)
      .map(_.getTableNames.asScala.toSeq)

  def createTable(tableName: String): Future[Unit] =
    instance
      .single(
        new CreateTableRequest()
          .withTableName(tableName)
          .withKeySchema(H("uid"), R("rid"))
          .withAttributeDefinitions(S("uid"), S("rid"))
          .withProvisionedThroughput(throughput)
          .toOp)
      .map(_ => Unit)
}
