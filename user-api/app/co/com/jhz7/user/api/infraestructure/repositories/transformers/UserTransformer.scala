package co.com.jhz7.user.api.infraestructure.repositories.transformers

import co.com.jhz7.user.api.application.dtos.UserDto
import co.com.jhz7.user.api.infraestructure.repositories.registries.UserRegistry

trait UserTransformer {

  def registryToDto( registry: UserRegistry ): UserDto =
    UserDto (
      firstName  = registry.dsFirstName,
      lastName   = registry.dsLastName,
      identifier = registry.cdIdentification
    )

  def dtoToRegistry( dto: UserDto ): UserRegistry =
    UserRegistry (
      dsFirstName      = dto.firstName,
      dsLastName       = dto.lastName,
      cdIdentification = dto.identifier
    )
}

object UserTransformer extends UserTransformer
