package co.com.jhz7.user.api.domain.models

final case class ErrorMessage( typeError: TypeError, message: String )

sealed trait TypeError

object APPLICATION extends TypeError {
  override def toString: String = "Error de tipo aplicación"
}

object TECHNICAL extends TypeError {
  override def toString: String = "Error de tipo técnico"
}

object BUSINESS extends TypeError {
  override def toString: String = "Error de tipo negocio"
}

object UNKNOWN extends TypeError {
  override def toString: String = "Error de tipo desconocido"
}
