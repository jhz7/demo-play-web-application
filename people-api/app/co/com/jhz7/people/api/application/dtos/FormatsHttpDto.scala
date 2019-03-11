package co.com.jhz7.people.api.application.dtos

import co.com.jhz7.people.api.infraestructure.repositories.registries.PeopleRegistry
import play.api.libs.json.{ Json, OFormat }

object FormatsHttpDto {

  implicit val formatPeopleRegistry: OFormat[PeopleRegistry] = Json.format[PeopleRegistry]
}
