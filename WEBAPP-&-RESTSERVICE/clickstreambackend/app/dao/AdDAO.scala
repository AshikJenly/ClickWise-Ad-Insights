package dao

import models._
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


case class AdDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

    private val dbConfig = dbConfigProvider.get[MySQLProfile]

    private class AdTable(tag:Tag) extends Table[AdModel](tag,"adstock"){

        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def adid = column[String]("adid")
        def adcontent = column[String]("adcontent")
        def adimage = column[String]("adimage")

        def * = (id.?, adid,adcontent,adimage) <> (AdModel.tupled, AdModel.unapply)
  
    }
    private val AllAds = TableQuery[AdTable]

     def getAllAds: Future[Seq[AdModel]] = {
            dbConfig.db.run(AllAds.result)
    }
}