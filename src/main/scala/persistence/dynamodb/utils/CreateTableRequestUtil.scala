package persistence.dynamodb.utils

import com.amazonaws.services.dynamodbv2.model._
import persistence.dynamodb.DynamoDBClient

object CreateTableRequestUtil {

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

  def createTableDefault(tableName: String): Boolean = {

    val xx = for {
      exists <- instance
        .single(listTables())
        .map(_.getTableNames.asScala.exists(_ == tableName))
    } yield {
      if (!exists) {
        new CreateTableRequest()
          .withTableName(tableName)
          .withKeySchema(H("uid"), R("rid"))
          .withAttributeDefinitions(S("uid"), S("rid"))
          .withProvisionedThroughput(throughput)
          .toOp
      }
    }

  }

  def listTables(): ListTables = new ListTablesRequest().toOp

}
