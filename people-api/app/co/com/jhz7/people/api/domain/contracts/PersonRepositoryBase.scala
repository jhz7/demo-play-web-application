package co.com.jhz7.people.api.domain.contracts

import akka.Done
import cats.data.Reader
import co.com.jhz7.people.api.application.CustomEitherT
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.domain.models.IdPerson
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait PersonRepositoryBase {

  def getPersonById( cdIdentification: IdPerson ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[PersonDto]]

  def deletePersonById( cdIdentification: IdPerson ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[ Done]]

  def savePerson( personToSave: PersonDto ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Done]]

  def getPeople: Reader[DatabaseConfig[JdbcProfile], CustomEitherT[List[PersonDto]]]
}
