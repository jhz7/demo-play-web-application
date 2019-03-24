package co.com.jhz7.user.api.domain.services

import akka.Done
import cats.implicits._
import co.com.jhz7.user.api.application._
import co.com.jhz7.user.api.application.dtos.UserDto
import co.com.jhz7.user.api.domain.models.{ BUSINESS, ErrorMessage }

object ValidateUserService {

  def validateUser( user: UserDto ): CustomEither[Done] =
    (
      validateFirstName( user.firstName),
      validateLastName( user.lastName ),
      validateId( user.identifier )
    ).mapN( (_, _, _) => Done ).toCustomEither

  private[services] def validateFirstName( firstName: String ): CustomValidatedNel[Done] =
    if( firstName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'primerNombre' es obligatorio. " ).invalidNel

  private[services] def validateLastName( lastName: String ): CustomValidatedNel[Done] =
    if( lastName.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'primerApellido' es obligatorio. " ).invalidNel

  private[services] def validateId( id: String ): CustomValidatedNel[Done] =
    if( id.nonEmpty ) Done.valid
    else ErrorMessage( BUSINESS, "El campo 'identificacion' es obligatorio. " ).invalidNel

}
