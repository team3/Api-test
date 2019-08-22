package api.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import api.model.{BiggestWord, CollectionPayload, SortedString, StringsComparisonResult}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val collectionPayloadFormat: RootJsonFormat[CollectionPayload] = jsonFormat1(CollectionPayload)
  implicit val biggestWordJsonFormat: RootJsonFormat[BiggestWord] = jsonFormat2(BiggestWord)
  implicit val sortedStringJsonFormat: RootJsonFormat[SortedString] = jsonFormat2(SortedString)
  implicit val stringsComparisonResultJsonFormat: RootJsonFormat[StringsComparisonResult] = jsonFormat1(StringsComparisonResult)
}
