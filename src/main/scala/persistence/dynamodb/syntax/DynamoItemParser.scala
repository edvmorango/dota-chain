package persistence.dynamodb.syntax

import com.amazonaws.services.dynamodbv2.model.AttributeValue

trait DynamoItemParser[A, B] {

  def fromModel(model: A): B
  def toModel(item: B): A
  def toItemMap(item: B): Map[String, AttributeValue]
}

trait DynamoItem[A]

object DynamoItemSyntax {

  implicit class DynamoItemSyntax[A](a: A) {
    def fromModel[B](model: A)(implicit p: DynamoItemParser[A, B]): B = {
      p.fromModel(model)
    }

    def toModel[A, B](item: B)(implicit p: DynamoItemParser[A, B]): A = {
      p.toModel(item)
    }
  }

}
