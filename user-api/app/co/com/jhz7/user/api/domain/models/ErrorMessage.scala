package co.com.jhz7.people.api.domain.models

final case class ErrorMessage( typeError: TypeError, message: String )

sealed trait TypeError

object APPLICATION extends TypeError
object TECHNICAL   extends TypeError
object BUSINESS    extends TypeError
object UNKNOWN     extends TypeError
