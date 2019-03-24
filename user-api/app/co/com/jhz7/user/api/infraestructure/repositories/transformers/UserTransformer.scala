package co.com.jhz7.people.api.infraestructure.repositories.transformers

import akka.Done
import cats.implicits._
import co.com.jhz7.people.api.application._
import co.com.jhz7.people.api.application.dtos.UserDto
import co.com.jhz7.people.api.domain.models.{ BUSINESS, ErrorMessage }
import co.com.jhz7.people.api.infraestructure.repositories.registries.UserRegistry

trait UserTransformer {

  def registryToDto( registry: UserRegistry ): CustomEither[UserDto] = {
    (
      validateFirstName( registry.dsFirstName),
      validateLastName( registry.dsLastName ),
      validateId( registry.cdIdentification )
    ).mapN( (_, _, _) =>
      UserDto(
        firstName  = registry.dsFirstName,
        lastName   = registry.dsLastName,
        identifier = registry.cdIdentification
      )
    ).toCustomEither
  }

  def dtoToRegistry( dto: UserDto ): CustomEither[UserRegistry] = {
    (
      validateFirstName( dto.firstName),
      validateLastName( dto.lastName ),
      validateId( dto.identifier )
    ).mapN( (_, _, _) =>
      UserRegistry(
        dsFirstName = dto.firstName,
        dsLastName = dto.lastName,
        cdIdentification = dto.identifier
      )
    ).toCustomEither
  }

  private[transformers] def validateFirstName( firstName: String ): CustomValidatedNel[Done] =
    if( firstName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'primerNombre' está vacío" ).invalidNel

  private[transformers] def validateLastName( lastName: String ): CustomValidatedNel[Done] =
    if( lastName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'primerApellido' está vacío" ).invalidNel

  private[transformers] def validateId( id: String ): CustomValidatedNel[Done] =
    if( id.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'identificacion' está vacío" ).invalidNel
}

object UserTransformer extends UserTransformer
