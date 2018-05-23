package persistence.dynamodb.syntax

import com.amazonaws.services.dynamodbv2.model.AttributeValue

trait DynamoItemParser[A] {

  def toMap(item: A): Map[String, AttributeValue]

}

object DynamoItemParserSyntax {

  implicit class DynamoItemSyntax[A](a: A) {

    def toMap()(
        implicit parser: DynamoItemParser[A]): Map[String, AttributeValue] =
      parser.toMap(a)

  }

}
