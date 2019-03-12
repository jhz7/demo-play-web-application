package co.com.jhz7.people.api.application.dtos

import co.com.jhz7.people.api.domain.models.IdPerson

case class PersonDto(
  firstName:  String,
  lastName:   String,
  identifier: IdPerson
)
