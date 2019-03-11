package co.com.jhz7.people.api.application

import co.com.jhz7.people.api.domain.contracts.PeopleRepositoryBase
import co.com.jhz7.people.api.infraestructure.DataBaseConfig
import co.com.jhz7.people.api.infraestructure.repositories.PeopleRepository
import javax.inject.{ Inject, Singleton }
import play.api.Configuration
import play.api.libs.ws.WSClient
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

@Singleton
class Dependencies @Inject() (val config: Configuration, val ws: WSClient){
  val databaseConfig: DatabaseConfig[JdbcProfile] = DataBaseConfig.dbConfigPostgres
  val repoPeople: PeopleRepositoryBase = PeopleRepository
}
