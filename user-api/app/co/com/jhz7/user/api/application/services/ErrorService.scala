package co.com.jhz7.user.api.application.services

import co.com.jhz7.user.api.domain.models._
import play.api.mvc.Result
import play.api.mvc.Results.{ BadRequest, InternalServerError }

trait ErrorService {

  def generateUniqueErrorMessage( errors: List[ErrorMessage] ): ErrorMessage = {
    val typeError = defineErrorType( errors )
    val message = errors.map(_.message).mkString("\n- ")
    ErrorMessage( typeError, "- " + message )
  }

  def generateHttpError( error: ErrorMessage ): Result = {
    error.errorType match {
      case BUSINESS | APPLICATION => BadRequest( error.message )
      case _                      => InternalServerError( error.message )
    }
  }

  private[services] def defineErrorType( errors: List[ErrorMessage] ): ErrorType = {
    val errorTypes = errors.map( _.errorType )
    ( errorTypes.contains( BUSINESS ), errorTypes.contains( APPLICATION ), errorTypes.contains( TECHNICAL ) ) match {
      case ( true, _, _ ) => BUSINESS
      case ( _, true, _ ) => APPLICATION
      case ( _, _, true ) => TECHNICAL
      case _              => APPLICATION
    }
  }
}

object ErrorService extends ErrorService
