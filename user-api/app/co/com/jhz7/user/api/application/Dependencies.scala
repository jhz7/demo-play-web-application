package co.com.jhz7.user.api.application

import co.com.jhz7.user.api.application.services.UsersPersistenceService
import co.com.jhz7.user.api.domain.contracts.UsersRepositoryBase
import co.com.jhz7.user.api.infraestructure.DataBaseConfig
import co.com.jhz7.user.api.infraestructure.repositories.UsersRepository
import javax.inject.{ Inject, Singleton }
import play.api.Configuration
import play.api.libs.ws.WSClient
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

@Singleton
class Dependencies @Inject() (val config: Configuration, val ws: WSClient){
  val databaseConfig: DatabaseConfig[JdbcProfile] = DataBaseConfig.dbConfigPostgres
  val repoUsers: UsersRepositoryBase = UsersRepository
  val usersPersistenceService: UsersPersistenceService = UsersPersistenceService
}
