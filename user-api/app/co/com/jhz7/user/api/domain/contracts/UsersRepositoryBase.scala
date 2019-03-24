package co.com.jhz7.user.api.domain.contracts

import cats.data.Reader
import co.com.jhz7.user.api.application.CustomEitherT
import co.com.jhz7.user.api.application.dtos.UserDto
import co.com.jhz7.user.api.domain.models.UserIdModel
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait UsersRepositoryBase {

  def getUserById( cdIdentification: UserIdModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Option[UserDto]]]

  def deleteUserById( cdIdentification: UserIdModel ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]]

  def saveUser( personToSave: UserDto ): Reader[DatabaseConfig[JdbcProfile], CustomEitherT[Int]]

  def getUsers: Reader[DatabaseConfig[JdbcProfile], CustomEitherT[List[UserDto]]]
}
