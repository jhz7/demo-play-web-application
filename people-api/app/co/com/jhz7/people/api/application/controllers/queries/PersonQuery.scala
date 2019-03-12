package co.com.jhz7.people.api.application.controllers.queries

import co.com.jhz7.people.api.application._
import co.com.jhz7.people.api.application.dtos.FormatsHttpDto._
import co.com.jhz7.people.api.application.services.ErrorService
import co.com.jhz7.people.api.domain.models.IdPersonModel
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }

@Singleton
case class PersonQuery @Inject()( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController {

  def getPerson( id: String ): Action[AnyContent] = Action.async(parse.anyContent) { _ =>
    val personId = IdPersonModel( id )
    dependencies.personPersistenceService
      .getPerson( personId ).run( dependencies )
      .fold(
        error => ErrorService.generateErrorHttp( error ),
        person => Ok( Json.toJson( person ) )
      ).runToFuture
  }

  def getPeople: Action[AnyContent] = Action.async(parse.anyContent) { _ =>
    dependencies.personPersistenceService
      .getPeople.run( dependencies )
      .fold(
        error  => ErrorService.generateErrorHttp( error ),
        people => Ok( Json.toJson( people ) )
      ).runToFuture
  }
}
