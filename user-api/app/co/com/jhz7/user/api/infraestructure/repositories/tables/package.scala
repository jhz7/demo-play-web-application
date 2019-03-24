package co.com.jhz7.user.api.infraestructure.repositories

import co.com.jhz7.user.api.infraestructure.repositories.registries.UserRegistry

package object tables {

  import slick.jdbc.PostgresProfile.api._

  val TB_People = TableQuery[TB_PEOPLE]

  class TB_PEOPLE( tag: Tag ) extends Table[UserRegistry]( tag, "tb_people" ){

    def dsFirstName = column[String]( "dsfirst_name" )

    def dsLastName = column[String]( "dslast_name" )

    def cdIdentification = column[String]( "cdidentification" )

    def pk = primaryKey( "tb_people_pk", cdIdentification )

    def * = ( cdIdentification, dsFirstName, dsLastName ) <> ( UserRegistry.tupled, UserRegistry.unapply )
  }
}
