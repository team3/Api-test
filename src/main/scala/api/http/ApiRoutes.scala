package api.http

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess, pathPrefix}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.util.Timeout
import api.model.{CollectionPayload, StringsComparisonResult}
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.Future

trait ApiRoutes extends JsonSupport {
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[ApiRoutes])

  def stringsProcessingActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val orgRoutes: Route = {
    pathPrefix("collection") {
      post {
        entity(as[CollectionPayload]) { strings =>
          val response: Future[StringsComparisonResult] = (stringsProcessingActor ? strings).mapTo[StringsComparisonResult]
          onSuccess(response) { success =>
            complete(StatusCodes.OK, success)
          }
        }
      }
    }
  }
}
