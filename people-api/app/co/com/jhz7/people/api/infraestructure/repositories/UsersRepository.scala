package co.com.jhz7.people.api.infraestructure.repositories

import cats.data.{ EitherT, Reader }
import cats.implicits._
import co.com.jhz7.people.api.application._
import co.com.jhz7.people.api.application.dtos.PersonDto
import co.com.jhz7.people.api.domain.contracts.PersonRepositoryBase
import co.com.jhz7.people.api.domain.models.{ ErrorMessage, IdPersonModel, TECHNICAL }
import co.com.jhz7.people.api.infraestructure.repositories.tables._
import co.com.jhz7.people.api.infraestructure.repositories.transformers.PersonTransformer
import monix.eval.Task
import play.api.Logger
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

trait PersonRepository extends PersonRepositoryBase {

  override def getPersonById( cdIdentification: IdPersonModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Option[PersonDto]]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>
      val sqlQuery = TB_People.filter ( _.cdIdentification === cdIdentification.id ).result

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map ( result => result.headOption.map( user => PersonTransformer.registryToDto( user ) ).traverseCustomEitherOption )
          .onErrorRecover [CustomEither[Option[PersonDto]]] {
            case error =>
              Logger.logger.error ( error.getMessage )
              ErrorMessage ( TECHNICAL, "Falló la consulta de persona por id" ).asLeft
          }
      }
  }

  override def deletePersonById( cdIdentification: IdPersonModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>
      val sqlQuery = TB_People.filter ( _.cdIdentification === cdIdentification.id ).delete

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map ( _.asRight )
          .onErrorRecover [CustomEither[Int]] {
            case error =>
              Logger.logger.error ( error.getMessage )
              ErrorMessage ( TECHNICAL, "Ocurrió un error al eliminar el registro de persona por id" ).asLeft
          }
      }
  }

  override def savePerson( personToSave: PersonDto ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>
      import dbConfig.profile.api._

      PersonTransformer.dtoToRegistry ( personToSave ).toCustomEitherT
        .flatMap ( registryToSave =>
          EitherT {
            Task.fromFuture ( dbConfig.db.run ( TB_People.insertOrUpdate ( registryToSave ) ) )
              .map ( _.asRight )
              .onErrorRecover [CustomEither[Int]] {
                case error =>
                  Logger.logger.error ( error.getMessage )
                  ErrorMessage ( TECHNICAL, "Ocurrió durante el guardado de los datos de persona" ).asLeft
              }
          }
        )
  }

  override def getPeople: Reader[DatabaseConfig[JdbcProfile], CustomEitherT[List[PersonDto]]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>
      val sqlQuery = TB_People.result

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map ( result => result.map ( PersonTransformer.registryToDto ).traverseCustomEitherSequence )
          .onErrorRecover [CustomEither[List[PersonDto]]] {
            case error =>
              Logger.logger.error ( error.getMessage )
              ErrorMessage ( TECHNICAL, "Falló la consulta de personas" ).asLeft
          }
      }
  }

}

object PersonRepository extends PersonRepository
