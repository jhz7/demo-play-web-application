package co.com.jhz7.people.api.application.controllers.queries

import co.com.jhz7.people.api.application.Dependencies
import javax.inject.{ Inject, Singleton }
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }

@Singleton
case class ProbeController @Inject() ( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController {

  def pp: Action[AnyContent] = Action(parse.anyContent) { _ =>
    Ok("Hello Word!!!")
  }
}
