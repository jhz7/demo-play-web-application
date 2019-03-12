package co.com.jhz7.people.api

import java.util.concurrent.Executors

import cats.data.{ EitherT, ValidatedNel }
import co.com.jhz7.people.api.application.services.ErrorService
import co.com.jhz7.people.api.domain.models.ErrorMessage
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

  implicit class CustomValidatedNelToCustomEither[T]( val validated: CustomValidatedNel[T] ) {
    def toCustomEither: CustomEither[T] =
      validated.fold(
        nonEmptyListError => Left( ErrorService.generateUniqueErrorMessage( nonEmptyListError.toList ) ),
        value             => Right( value )
      )
  }

  implicit class CustomEitherFromSequence[T]( val seqCustomEither: Seq[CustomEither[T]] ) {
    def traverseCustomEitherSequence: CustomEither[List[T]] = {
      seqCustomEither.foldRight( Right( Nil ): CustomEither[List[T]] ) {
        ( value, acc ) => for ( xs <- acc.right; x <- value.right ) yield x :: xs
      }
    }
  }

  implicit class CastToCustomEitherT[T]( val customEither: CustomEither[T] ) {
    def toCustomEitherT: CustomEitherT[T] = {
      EitherT.fromEither( customEither )
    }
  }
}
