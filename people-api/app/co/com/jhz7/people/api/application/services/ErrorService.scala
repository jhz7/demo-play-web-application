package co.com.jhz7.people.api.application.services

import co.com.jhz7.people.api.domain.models._
import play.api.mvc.Result
import play.api.mvc.Results.{ BadRequest, InternalServerError }

trait ErrorService {

  def generateUniqueErrorMessage( errors: List[ErrorMessage] ): ErrorMessage = {
    val typeError = defineType( errors )
    val message = errors.map(_.message).mkString("- ")
    ErrorMessage( typeError, message )
  }

  def generateErrorHttp( error: ErrorMessage ): Result = {
    error.typeError match {
      case BUSINESS => BadRequest( error.message )
      case _        => InternalServerError( error.message )
    }
  }

  private[services] def defineType( errors: List[ErrorMessage] ): TypeError = {
    val errorTypes = errors.map( _.typeError )
    ( errorTypes.contains( BUSINESS ), errorTypes.contains( APPLICATION ), errorTypes.contains( TECHNICAL ) ) match {
      case ( true, _, _ ) => BUSINESS
      case ( _, true, _ ) => APPLICATION
      case ( _, _, true ) => TECHNICAL
      case _              => APPLICATION
    }
  }
}

object ErrorService extends ErrorService
