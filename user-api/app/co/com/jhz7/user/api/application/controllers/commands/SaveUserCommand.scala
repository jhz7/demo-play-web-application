package co.com.jhz7.user.api.application.controllers.commands

import co.com.jhz7.user.api.application.{ Dependencies, _ }
import co.com.jhz7.user.api.application.dtos.FormatsHttpDto._
import co.com.jhz7.user.api.application.dtos.{ SuccessResponseDto, UserDto }
import co.com.jhz7.user.api.application.services.ErrorService
import javax.inject.{ Inject, Singleton }
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }

@Singleton
case class SaveUserCommand @Inject()( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController with CommandHelper {

  def execute: Action[AnyContent] = Action.async(parse.anyContent) {
    request =>
      request.getDataAsEither[UserDto].toCustomEitherT
        .flatMap{ userDto => dependencies.usersPersistenceService.saveUser( userDto ).run( dependencies ) }
        .fold(
          error => {
            Logger.logger.error( error.typeError.toString + error.message )
            ErrorService.generateErrorHttp( error )
          },
          _     => Ok( Json.toJson( SuccessResponseDto( date = new DateTime(), message = "Datos guardados con éxito" ) ) )
        ).runToFuture
  }
}
