package co.com.jhz7.people.api.application.controllers.commands

import co.com.jhz7.people.api.application.{Dependencies, _}
import co.com.jhz7.people.api.application.dtos.FormatsHttpDto._
import co.com.jhz7.people.api.application.dtos.{IdPersonDto, SuccessResponseDto}
import co.com.jhz7.people.api.application.services.ErrorService
import co.com.jhz7.people.api.domain.models.IdPersonModel
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

@Singleton
case class DeletePersonCommand @Inject()( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController with CommandHelper {

  def execute: Action[AnyContent] = Action.async(parse.anyContent) {
    request =>
      request.getDataAsEither[IdPersonDto].toCustomEitherT
        .flatMap{ idPersonDto =>
          dependencies.personPersistenceService
            .deletePerson( IdPersonModel( idPersonDto.id ) ).run( dependencies )
        }
        .fold(
          error => ErrorService.generateErrorHttp( error ),
          _     => Ok( Json.toJson( SuccessResponseDto( date = new DateTime(), message = "Los datos de la persona fueron eliminados con éxito" ) ) )
        ).runToFuture
  }
}
