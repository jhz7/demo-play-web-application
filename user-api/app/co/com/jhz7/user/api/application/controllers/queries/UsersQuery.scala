package co.com.jhz7.user.api.application.controllers.queries

import co.com.jhz7.user.api.application._
import co.com.jhz7.user.api.application.dtos.FormatsHttpDto._
import co.com.jhz7.user.api.application.services.ErrorService
import co.com.jhz7.user.api.domain.models.UserIdModel
import javax.inject.{ Inject, Singleton }
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }

@Singleton
case class UsersQuery @Inject()( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController {

  def getUser( id: String ): Action[AnyContent] = Action.async(parse.anyContent) { _ =>
    val personId = UserIdModel( id )
    dependencies.usersPersistenceService
      .getUser( personId ).run( dependencies )
      .fold(
        error => {
          Logger.logger.error( error.errorType.toString + error.message )
          ErrorService.generateErrorHttp( error )
        },
        {
          case Some( user ) => Ok( Json.toJson( user ) )
          case None         => NoContent
        }
      ).runToFuture
  }

  def getUsers: Action[AnyContent] = Action.async(parse.anyContent) { _ =>
    dependencies.usersPersistenceService
      .getUsers.run( dependencies )
      .fold(
        error  => {
          Logger.logger.error( error.errorType.toString + error.message )
          ErrorService.generateErrorHttp( error )
        },
        {
          case Nil   => NoContent
          case users => Ok( Json.toJson( users ) )
        }
      ).runToFuture
  }
}
