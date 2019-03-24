package co.com.jhz7.user.api.infraestructure.repositories

import cats.data.{ EitherT, Reader }
import cats.implicits._
import co.com.jhz7.user.api.application._
import co.com.jhz7.user.api.application.dtos.UserDto
import co.com.jhz7.user.api.domain.contracts.UsersRepositoryBase
import co.com.jhz7.user.api.domain.models.{ ErrorMessage, TECHNICAL, UserIdModel }
import co.com.jhz7.user.api.infraestructure.repositories.tables._
import co.com.jhz7.user.api.infraestructure.repositories.transformers.UserTransformer
import monix.eval.Task
import play.api.Logger
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

trait UsersRepository extends UsersRepositoryBase {

  override def getUserById( cdIdentification: UserIdModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Option[UserDto]]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>

      val sqlQuery = TB_People.filter ( _.cdIdentification === cdIdentification.id ).result

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map( result => result.headOption.map( user => UserTransformer.registryToDto( user ) ).asRight )
          .onErrorRecover [CustomEither[Option[UserDto]]] {
            case error =>
              Logger.logger.error( error.getMessage )
              ErrorMessage ( TECHNICAL, "Falló la consulta de usuario por id" ).asLeft
          }
      }
  }

  override def deleteUserById( cdIdentification: UserIdModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>

      val sqlQuery = TB_People.filter ( _.cdIdentification === cdIdentification.id ).delete

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map( _.asRight )
          .onErrorRecover [CustomEither[Int]] {
            case error =>
              Logger.logger.error( error.getMessage )
              ErrorMessage ( TECHNICAL, "Ocurrió un error al eliminar el registro de usuario por id" ).asLeft
          }
      }
  }

  override def saveUser( userToSave: UserDto ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>

      import dbConfig.profile.api._

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( TB_People.insertOrUpdate ( UserTransformer.dtoToRegistry( userToSave ) ) ) )
          .map( _.asRight )
          .onErrorRecover [CustomEither[Int]] {
            case error =>
              Logger.logger.error( error.getMessage )
              ErrorMessage ( TECHNICAL, "Ocurrió durante el guardado de los datos del usuario" ).asLeft
          }
      }
  }

  override def getUsers: Reader[DatabaseConfig[JdbcProfile], CustomEitherT[List[UserDto]]] = Reader {
    dbConfig: DatabaseConfig[JdbcProfile] =>

      val sqlQuery = TB_People.result

      EitherT {
        Task.fromFuture ( dbConfig.db.run ( sqlQuery ) )
          .map( result => result.map( UserTransformer.registryToDto ).toList.asRight )
          .onErrorRecover [CustomEither[List[UserDto]]] {
            case error =>
              Logger.logger.error( error.getMessage )
              ErrorMessage ( TECHNICAL, "Falló la consulta de usuarios" ).asLeft
          }
      }
  }

}

object UsersRepository extends UsersRepository
