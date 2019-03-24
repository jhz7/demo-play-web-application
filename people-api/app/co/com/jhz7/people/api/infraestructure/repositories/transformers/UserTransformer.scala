package co.com.jhz7.people.api.infraestructure.repositories.transformers

import akka.Done
import co.com.jhz7.people.api.application._
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.domain.models.{ BUSINESS, ErrorMessage, IdPersonModel }
import co.com.jhz7.people.api.infraestructure.repositories.registries.PersonRegistry
import cats.implicits._

trait PersonTransformer {

  def registryToDto( registry: PersonRegistry ): CustomEither[PersonDto] = {
    (
      validateFirstName( registry.dsFirstName),
      validateLastName( registry.dsLastName ),
      validateId( registry.cdIdentification )
    ).mapN( (_, _, _) =>
      PersonDto(
        firstName  = registry.dsFirstName,
        lastName   = registry.dsLastName,
        identifier = registry.cdIdentification
      )
    ).toCustomEither
  }

  def dtoToRegistry( dto: PersonDto ): CustomEither[PersonRegistry] = {
    (
      validateFirstName( dto.firstName),
      validateLastName( dto.lastName ),
      validateId( dto.identifier )
    ).mapN( (_, _, _) =>
      PersonRegistry(
        dsFirstName = dto.firstName,
        dsLastName = dto.lastName,
        cdIdentification = dto.identifier
      )
    ).toCustomEither
  }

  private[transformers] def validateFirstName( firstName: String ): CustomValidatedNel[Done] =
    if( firstName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "The firstName field is empty" ).invalidNel

  private[transformers] def validateLastName( lastName: String ): CustomValidatedNel[Done] =
    if( lastName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "The lastName field is empty" ).invalidNel

  private[transformers] def validateId( id: String ): CustomValidatedNel[Done] =
    if( id.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "The identification field is empty" ).invalidNel
}

object PersonTransformer extends PersonTransformer
