package co.com.jhz7.people.api.application.dtos

import org.joda.time.DateTime
import play.api.libs.json._

object FormatsHttpDto {

  implicit val writesDateTime: Writes[DateTime] = Writes {
    value: DateTime =>
      JsString( value.toLocalDateTime.toString("yyyy-MM-dd HH:mm:ss") )
  }

  implicit val formatIdPersonDto: OFormat[IdPersonDto] = Json.format[IdPersonDto]

  implicit val formatPersonDto: OFormat[PersonDto] = Json.format[PersonDto]

  implicit val writesSuccessResponseDto: OWrites[SuccessResponseDto] = Json.writes[SuccessResponseDto]

}
