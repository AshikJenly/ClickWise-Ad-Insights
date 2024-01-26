package dao

import models._
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


case class UsersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

    private val dbConfig = dbConfigProvider.get[MySQLProfile]
    private class UsersTable(tag:Tag) extends Table[User](tag,"users"){

        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
        def name = column[String]("name")
        def email = column[String]("email")
        def password = column[String]("password")

        def * = (id.?, name,email,password) <> (User.tupled, User.unapply)
  
    }
    private val Users = TableQuery[UsersTable]
    


    def getAllUsers: Future[Seq[User]] = {
            dbConfig.db.run(Users.result)
    }
    def createUser(user: User): Future[Unit] = {
        dbConfig.db.run(Users += user).map(_ => ())
  }

}