package co.com.jhz7.people.api.application.controllers.commands

import cats.implicits._
import co.com.jhz7.people.api.application.CustomEither
import co.com.jhz7.people.api.domain.models.{APPLICATION, ErrorMessage}
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}
import play.api.mvc.{AnyContent, Request}

trait CommandHelper {
  implicit class ReqAsCustomEither( val request: Request[AnyContent] ) {
    def getDataAsEither[T]( implicit reads: Reads[T] ): CustomEither[T] = {
      request.body.asJson match {
        case Some( json ) => Json.fromJson[T]( json ) match {
          case JsSuccess( obj, _ ) => obj.asRight
          case JsError( errors )   =>
            val message = "- " + errors.map( v => v._1.toJsonString + " " + v._2.map( _.message ).mkString("\n- ") ).mkString("- ").replace( '.', ' ' )
            ErrorMessage( APPLICATION, message ).asLeft
        }
        case None => ErrorMessage( APPLICATION, "No se especificó una petición en formato JSON" ).asLeft
      }
    }
  }
}
