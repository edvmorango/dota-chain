package persistence.dynamodb.utils

import com.amazonaws.services.dynamodbv2.model.{
  AttributeDefinition,
  KeySchemaElement,
  KeyType,
  ScalarAttributeType
}

object CreateTableRequestUtil {

  import KeyType._

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

}
