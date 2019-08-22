package api.model

case class CollectionPayload(strings: Seq[String])

case class BiggestWord(value: String, length: Int)

case class SortedString(string: String, biggestWord: BiggestWord)

case class StringsComparisonResult(result: Seq[SortedString])