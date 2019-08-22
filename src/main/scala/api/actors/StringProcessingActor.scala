package api.actors

import akka.actor.{Actor, ActorLogging, Props}
import api.model.{BiggestWord, CollectionPayload, SortedString, StringsComparisonResult}

object StringProcessingActor {

  case class StringsProcessingRequest(payload: CollectionPayload)

  def props: Props = Props[StringProcessingActor]
}

class StringProcessingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case CollectionPayload(strings) =>
      sender() ! StringsComparisonResult(sort(strings))
    case _ => log.error("Unsupported message")
  }

  def sort(strings: Seq[String]): Seq[SortedString] = {
    strings.map(s => SortedString(s, biggestWord(s))).sortBy(-_.biggestWord.length)
  }

  def biggestWord(string: String): BiggestWord = {
    if (string == null) BiggestWord(null, 0) else {
      val biggestWord = string.split("\\s").maxBy(_.length)
      BiggestWord(biggestWord, biggestWord.length)
    }
  }
}
