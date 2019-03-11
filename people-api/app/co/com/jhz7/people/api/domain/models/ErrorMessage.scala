package co.com.jhz7.people.api.domain.models

import play.api.libs.json.{ Json, OFormat }

final case class ErrorMessage( typeError: String, message: String )

object ErrorMessage {
  implicit val formatErrorMessage: OFormat[ErrorMessage] = Json.format[ErrorMessage]
}
