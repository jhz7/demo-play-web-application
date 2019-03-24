package co.com.jhz7.people.api.application.services

import akka.Done
import cats.data.Reader
import cats.implicits._
import co.com.jhz7.people.api.application.dtos.UserDto
import co.com.jhz7.people.api.application.{ CustomEitherT, Dependencies }
import co.com.jhz7.people.api.domain.models.{ APPLICATION, ErrorMessage, UserIdModel }

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

  def saveUser( personToSave: UserDto ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoUsers
        .saveUser ( personToSave ).run ( dependencies.databaseConfig )
        .subflatMap { modifiedRegistriesAmount =>
          if ( modifiedRegistriesAmount > 0 ) Done.asRight
          else ErrorMessage ( APPLICATION, "No se guardaron los datos para el usuario. " ).asLeft
        }
  }

  def getUsers: Reader[Dependencies, CustomEitherT[List[UserDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoUsers
        .getUsers.run ( dependencies.databaseConfig )
  }

}

object UsersPersistenceService extends UsersPersistenceService
