package api

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.http.ApiRoutes
import api.model.CollectionPayload
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class ApiRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
        with ApiRoutes {
  override val stringsProcessingActor: ActorRef =
    system.actorOf(actors.StringProcessingActor.props, "userRegistry")

  lazy val routes: Route = orgRoutes

  "Org routes should" should {
    "return empty response if input payload is empty" in {
      val strings = CollectionPayload(Seq.empty)
      val entity = Marshal(strings).to[MessageEntity].futureValue

      // using the RequestBuilding DSL:
      val request = Post("/collection").withEntity(entity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("""{"result":[]}""")
      }
    }

    "return correct response if input payload contains several strings" in {
      val strings = CollectionPayload(Seq(
        "Sound boy proceed to blast into the galaxy",
        "Go back rocket man into the sky you'll see",
        "Hear it all the time, come back rewind",
        "Aliens are watching up in the sky",
        "Sound boy process to blast into the galaxy",
        "No one gonna harm you",
        "They all want you to play I watch the birds of prey"))

      val entity = Marshal(strings).to[MessageEntity].futureValue

      // using the RequestBuilding DSL:
      val request = Post("/collection").withEntity(entity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("""{"result":[{"biggestWord":{"length":8,"value":"watching"},"string":"Aliens are watching up in the sky"},{"biggestWord":{"length":7,"value":"proceed"},"string":"Sound boy proceed to blast into the galaxy"},{"biggestWord":{"length":7,"value":"process"},"string":"Sound boy process to blast into the galaxy"},{"biggestWord":{"length":6,"value":"rocket"},"string":"Go back rocket man into the sky you'll see"},{"biggestWord":{"length":6,"value":"rewind"},"string":"Hear it all the time, come back rewind"},{"biggestWord":{"length":5,"value":"gonna"},"string":"No one gonna harm you"},{"biggestWord":{"length":5,"value":"watch"},"string":"They all want you to play I watch the birds of prey"}]}""")
      }
    }
  }
}
