package co.com.jhz7.people.api.domain.contracts

import cats.data.{ EitherT, Reader }
import co.com.jhz7.people.api.domain.models.ErrorMessage
import co.com.jhz7.people.api.infraestructure.repositories.registries.PeopleRegistry
import monix.eval.Task
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait PeopleRepositoryBase {
  def getPeoplebyId( cdIdentification: String ): Reader[DatabaseConfig[JdbcProfile], EitherT[Task, ErrorMessage, PeopleRegistry]]
}
