package co.com.jhz7.people.api.application.dtos

import play.api.libs.json.{Json, OFormat}

object FormatsHttpDto {

  implicit val formatPersonDto: OFormat[PersonDto] = Json.format[PersonDto]

}
