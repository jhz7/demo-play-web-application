package co.com.jhz7.user.api.domain.models

final case class ErrorMessage( errorType: ErrorType, message: String )

sealed trait ErrorType

object APPLICATION extends ErrorType {
  override def toString: String = "Error de tipo aplicación"
}

object TECHNICAL extends ErrorType {
  override def toString: String = "Error de tipo técnico"
}

object BUSINESS extends ErrorType {
  override def toString: String = "Error de tipo negocio"
}

object UNKNOWN extends ErrorType {
  override def toString: String = "Error de tipo desconocido"
}
