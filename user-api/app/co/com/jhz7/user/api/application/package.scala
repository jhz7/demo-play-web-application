package co.com.jhz7.user.api

import java.util.concurrent.Executors

import cats.data.{ EitherT, ValidatedNel }
import co.com.jhz7.user.api.application.services.ErrorService
import co.com.jhz7.user.api.domain.models.ErrorMessage
import monix.eval.Task
import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.UncaughtExceptionReporter
import monix.execution.schedulers.ExecutorScheduler

package object application {

  type CustomEitherT[T] = EitherT[Task, ErrorMessage, T]

  type CustomEither[T] = Either[ErrorMessage, T]

  type CustomValidatedNel[T] = ValidatedNel[ErrorMessage, T]

  implicit val executionScheduler: ExecutorScheduler = ExecutorScheduler(
    Executors.newFixedThreadPool( 10 ),
    UncaughtExceptionReporter( t => println( s"this should not happen: ${t.getMessage}" ) ),
    AlwaysAsyncExecution
  )

  implicit class CustomValidatedNelToCustomEither[T]( val validated: CustomValidatedNel[T] ) extends AnyVal {
    def toCustomEither: CustomEither[T] =
      validated.fold(
        nonEmptyListError => Left( ErrorService.generateUniqueErrorMessage( nonEmptyListError.toList ) ),
        value             => Right( value )
      )
  }

  implicit class CustomEitherFromOption[T] ( val value: Option[CustomEither[T]] ) {
    def traverseCustomEitherOption: CustomEither[Option[T]] = {
      value match {
        case Some( either ) => either.map( v => Some(v) )
        case _ => Right(None)
      }
    }
  }

  implicit class CustomEitherFromSequence[T]( val seqCustomEither: Seq[CustomEither[T]] ) extends AnyVal {
    def traverseCustomEitherSequence: CustomEither[List[T]] = {
      seqCustomEither.foldRight( Right( Nil ): CustomEither[List[T]] ) {
        ( value, acc ) => for ( xs <- acc.right; x <- value.right ) yield x :: xs
      }
    }
  }

  implicit class CastToCustomEitherT[T]( val customEither: CustomEither[T] ) extends AnyVal {
    def toCustomEitherT: CustomEitherT[T] = {
      EitherT.fromEither( customEither )
    }
  }
}
