package co.com.jhz7.people.api.application

import co.com.jhz7.people.api.application.services.PersonPersistenceService
import co.com.jhz7.people.api.domain.contracts.PersonRepositoryBase
import co.com.jhz7.people.api.infraestructure.DataBaseConfig
import co.com.jhz7.people.api.infraestructure.repositories.PersonRepository
import javax.inject.{ Inject, Singleton }
import play.api.Configuration
import play.api.libs.ws.WSClient
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

@Singleton
class Dependencies @Inject() (val config: Configuration, val ws: WSClient){
  val databaseConfig: DatabaseConfig[JdbcProfile] = DataBaseConfig.dbConfigPostgres
  val repoPeople: PersonRepositoryBase = PersonRepository
  val personPersistenceService: PersonPersistenceService = PersonPersistenceService
}
