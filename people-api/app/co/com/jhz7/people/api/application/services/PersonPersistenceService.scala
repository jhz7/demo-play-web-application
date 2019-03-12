package co.com.jhz7.people.api.application.services

import akka.Done
import cats.data.Reader
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.application.{ CustomEitherT, Dependencies }
import co.com.jhz7.people.api.domain.models.IdPerson

trait PersonPersistenceService {

  def getPerson( id: IdPerson ): Reader[Dependencies, CustomEitherT[PersonDto]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .getPersonById( id ).run( dependencies.databaseConfig )
  }

  def deletePerson( id: IdPerson ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .deletePersonById( id ).run( dependencies.databaseConfig )
  }

  def savePerson( personToSave: PersonDto ): Reader[Dependencies, CustomEitherT[Done]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .savePerson( personToSave ).run( dependencies.databaseConfig )
  }

  def getPeople: Reader[Dependencies, CustomEitherT[List[PersonDto]]] = Reader {
    dependencies: Dependencies =>
      dependencies.repoPeople
        .getPeople.run( dependencies.databaseConfig )
  }

}

object PersonPersistenceService extends PersonPersistenceService
