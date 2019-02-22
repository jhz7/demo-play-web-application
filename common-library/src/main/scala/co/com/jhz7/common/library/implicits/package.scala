package co.com.jhz7.common.library

import play.api.mvc._
import play.api.libs.json._

package object implicits {

  implicit class JsonRequestOptions( request: Request[AnyContent] ) {

    def jsonAs[A](implicit reads: Reads[A]): JsResult[A] =
      request.body.asJson match {
        case Some(json) => Json.fromJson[A](json)
        case None       => JsError(JsPath, "No JSON specified")
      }
  }
}
