package co.com.jhz7.people.api.domain.contracts

import cats.data.Reader
import co.com.jhz7.people.api.application.CustomEitherT
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.domain.models.IdPersonModel
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait PersonRepositoryBase {

  def getPersonById( cdIdentification: IdPersonModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Option[PersonDto]]]

  def deletePersonById( cdIdentification: IdPersonModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]]

  def savePerson( personToSave: PersonDto ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]]

  def getPeople: Reader[DatabaseConfig[JdbcProfile], CustomEitherT[List[PersonDto]]]
}
