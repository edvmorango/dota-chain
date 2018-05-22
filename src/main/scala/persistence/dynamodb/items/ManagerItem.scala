package persistence.dynamodb.items

import model.Entities.{Manager, UID}

case class ManagerItem(uid: UID, rid: String, name: String, nickname: String) {}

object ManagerItem {

  private val rk = "rangeKey"

  def toModel(i: ManagerItem): Manager =
    Manager.apply(i.uid, i.name, i.nickname)

  def fromModel(m: Manager): ManagerItem =
    ManagerItem(m.uid, rk, m.name, m.nickname)

}
