package co.com.jhz7.user.api.application.services

import akka.Done
import cats.data.Reader
import cats.implicits._
import co.com.jhz7.user.api.application.dtos.UserDto
import co.com.jhz7.user.api.application._
import co.com.jhz7.user.api.domain.models.{ APPLICATION, ErrorMessage, UserIdModel }
import co.com.jhz7.user.api.domain.services.ValidateUserService

trait UsersPersistenceService {

  def getUser( id: UserIdModel ): Reader[Dependencies, CustomEitherT[Option[UserDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoUsers
        .getUserById ( id ).run ( dependencies.databaseConfig )
  }

  def deleteUser( id: UserIdModel ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoUsers
        .deleteUserById ( id ).run ( dependencies.databaseConfig )
        .subflatMap { modifiedRegistriesAmount =>
          if ( modifiedRegistriesAmount > 0 ) Done.asRight
          else ErrorMessage ( APPLICATION, s"El usuario con identificación ${id.id} no existe y por lo tanto, no se eliminó información. " ).asLeft
        }
  }

  def saveUser( userToSave: UserDto ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      ValidateUserService.validateUser( userToSave ).toCustomEitherT
        .flatMap( _ =>
          dependencies.repoUsers
            .saveUser ( userToSave ).run ( dependencies.databaseConfig )
            .subflatMap { modifiedRegistriesAmount =>
              if ( modifiedRegistriesAmount > 0 ) Done.asRight
              else ErrorMessage ( APPLICATION, "No se guardaron los datos para el usuario. " ).asLeft
            }
        )
  }

  def getUsers: Reader[Dependencies, CustomEitherT[List[UserDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoUsers
        .getUsers.run ( dependencies.databaseConfig )
  }

}

object UsersPersistenceService extends UsersPersistenceService
