package co.com.jhz7.people.api.application.dtos

import org.joda.time.DateTime
import play.api.libs.json._

object FormatsHttpDto {

  implicit val writesDateTime: Writes[DateTime] = Writes {
    value: DateTime =>
      JsString( value.toLocalDateTime.toString("yyyy-MM-dd HH:mm:ss") )
  }

  implicit val formatUserIdDto: OFormat[UserIdDto] = Json.format[UserIdDto]

  implicit val formatUserDto: OFormat[UserDto] = Json.format[UserDto]

  implicit val writesSuccessResponseDto: OWrites[SuccessResponseDto] = Json.writes[SuccessResponseDto]

}
