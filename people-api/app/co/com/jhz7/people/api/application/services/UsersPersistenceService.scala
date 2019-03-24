package co.com.jhz7.people.api.application.services

import akka.Done
import cats.data.Reader
import cats.implicits._
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.application.{ CustomEitherT, Dependencies }
import co.com.jhz7.people.api.domain.models.{ APPLICATION, ErrorMessage, IdPersonModel }

trait PersonPersistenceService {

  def getPerson( id: IdPersonModel ): Reader[Dependencies, CustomEitherT[Option[PersonDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .getPersonById ( id ).run ( dependencies.databaseConfig )
  }

  def deletePerson( id: IdPersonModel ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .deletePersonById ( id ).run ( dependencies.databaseConfig )
        .subflatMap { modifiedRegistriesAmount =>
          if ( modifiedRegistriesAmount > 0 ) Done.asRight
          else ErrorMessage ( APPLICATION, s"El usuario con identificación ${id.id} no existe y por lo tanto, no se eliminó información. " ).asLeft
        }
  }

  def savePerson( personToSave: PersonDto ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .savePerson ( personToSave ).run ( dependencies.databaseConfig )
        .subflatMap { modifiedRegistriesAmount =>
          if ( modifiedRegistriesAmount > 0 ) Done.asRight
          else ErrorMessage ( APPLICATION, "No se guardaron los datos para el usuario. " ).asLeft
        }
  }

  def getPeople: Reader[Dependencies, CustomEitherT[List[PersonDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .getPeople.run ( dependencies.databaseConfig )
  }

}

object PersonPersistenceService extends PersonPersistenceService
