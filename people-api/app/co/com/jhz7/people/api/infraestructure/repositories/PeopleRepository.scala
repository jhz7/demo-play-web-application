package co.com.jhz7.people.api.infraestructure.repositories

import cats.data.{ EitherT, Reader }
import co.com.jhz7.people.api.domain.contracts.PeopleRepositoryBase
import co.com.jhz7.people.api.domain.models.ErrorMessage
import co.com.jhz7.people.api.infraestructure.repositories.registries.PeopleRegistry
import co.com.jhz7.people.api.infraestructure.repositories.tables._
import monix.eval.Task
import play.api.Logger
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import cats.implicits._

trait PeopleRepository extends PeopleRepositoryBase{

  def getPeoplebyId( cdIdentification: String ): Reader[DatabaseConfig[JdbcProfile], EitherT[Task, ErrorMessage, PeopleRegistry]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>

      val sqlQuery = TB_People.filter( _.cdIdentification === cdIdentification ).result

      EitherT {
        Task.fromFuture( dbConfig.db.run( sqlQuery ) )
          .map( result => result.headOption match {
            case Some(registry) => registry.asRight
            case None => ErrorMessage( "Application", "No se consultó el registro" ).asLeft
          } )
          .onErrorRecover[Either[ErrorMessage, PeopleRegistry]]{
            case error =>
              Logger.logger.error(error.getMessage)
              ErrorMessage("Tecnico", "Falló la consulta de persona por id").asLeft
          }
      }
  }
}

object PeopleRepository extends PeopleRepository
