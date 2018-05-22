package persistence.dynamodb.syntax

import com.amazonaws.services.dynamodbv2.model.AttributeValue

trait DynamoItemParser[A] {

  def toMap(item: A): Map[String, AttributeValue]

}

object DynamoItemSyntax {

  implicit class DynamoItemSyntax[A](parser: DynamoItemParser[A]) {

    def toMap(item: A): Map[String, AttributeValue] = parser.toMap(item)

  }

}
