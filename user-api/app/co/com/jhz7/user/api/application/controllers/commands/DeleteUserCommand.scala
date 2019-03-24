package co.com.jhz7.user.api.application.controllers.commands

import co.com.jhz7.user.api.application.{Dependencies, _}
import co.com.jhz7.user.api.application.dtos.FormatsHttpDto._
import co.com.jhz7.user.api.application.dtos.{UserIdDto, SuccessResponseDto}
import co.com.jhz7.user.api.application.services.ErrorService
import co.com.jhz7.user.api.domain.models.UserIdModel
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

@Singleton
case class DeleteUserCommand @Inject()( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController with CommandHelper {

  def execute: Action[AnyContent] = Action.async(parse.anyContent) {
    request =>
      request.getDataAsEither[UserIdDto].toCustomEitherT
        .flatMap{ userIdDto => dependencies.usersPersistenceService.deleteUser( UserIdModel( userIdDto.id ) ).run( dependencies ) }
        .fold(
          error => ErrorService.generateErrorHttp( error ),
          _     => Ok( Json.toJson( SuccessResponseDto( date = new DateTime(), message = "Los datos del usuario fueron eliminados con éxito" ) ) )
        ).runToFuture
  }
}
