package co.com.jhz7.people.api.application.controllers.queries

import java.util.concurrent.Executors

import co.com.jhz7.people.api.application.Dependencies
import co.com.jhz7.people.api.application.dtos.FormatsHttpDto._
import javax.inject.{ Inject, Singleton }
import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.UncaughtExceptionReporter
import monix.execution.schedulers.ExecutorScheduler
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent, BaseController, ControllerComponents }

import scala.concurrent.ExecutionContext

@Singleton
case class ProbeController @Inject() ( dependencies: Dependencies, controllerComponents: ControllerComponents ) extends BaseController {

  private implicit val commandExecutionContext: ExecutorScheduler = ExecutorScheduler(
    Executors.newFixedThreadPool( 25 ),
    UncaughtExceptionReporter( t => println( s"this should not happen: ${t.getMessage}" ) ),
    AlwaysAsyncExecution
  )

  implicit val x = ExecutionContext.fromExecutorService( Executors.newFixedThreadPool( 25 ) )

  def pp( personid: String ): Action[AnyContent] = Action.async(parse.anyContent) { _ =>
    dependencies.repoPeople
      .getPeoplebyId( personid ).run( dependencies.databaseConfig )
      .fold(
        error => InternalServerError( error.message ),
        person => Ok( Json.toJson( person ) )
      ).runToFuture
  }
}
