package co.com.jhz7.people.api.application.dtos

import co.com.jhz7.people.api.domain.models.IdPerson
import play.api.libs.json.{ Json, OFormat }

object FormatsHttpDto {

  implicit val formatIdPerson: OFormat[IdPerson] = Json.format[IdPerson]

  implicit val formatPersonDto: OFormat[PersonDto] = Json.format[PersonDto]

}
