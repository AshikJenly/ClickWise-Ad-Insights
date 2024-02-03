// app/controllers/MongoDBController.scala

package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Sorts._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoDBController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Replace "YOUR_CONNECTION_STRING" with your actual connection string
  val connectionString = "mongodb://forspark:J6cznn1Wd7ukY4lNNxqhhyJxVRm4aBIH8WrAqrHspTjbKTW5KvrIEqHdL9SvsyLmZziicHF4M3v8ACDbna0CdA==@forspark.mongo.cosmos.azure.com:10255/?ssl=true&retrywrites=false&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@forspark@"
  val client: MongoClient = MongoClient(connectionString)
  val database: MongoDatabase = client.getDatabase("click")
  val collection: MongoCollection[Document] = database.getCollection("test")

  def getAllDocuments: Action[AnyContent] = Action.async { _ =>
    // val projection = include("field1", "field2") // Specify the fields you want to retrieve
   
    collection.find().toFuture().map { docs =>
      Ok(docs.map(_.toJson()).mkString("[", ",", "]"))
    }
  }

  def aggGroupByWindow: Action[AnyContent] = Action.async { _ =>
    val aggregationPipeline = Seq(
       group("$window",
        sum("totalUsersVisited", "$total_users_visited"),
        sum("totalUniqueUsersVisited", "$total_unique_users_visited"),
        avg("avgTimeSpendInWebsite", "$avg_time_spend_in_website"),
        addToSet("addWatched", "$add_watched")
      ),
      sort(descending("_id")),
      limit(10)
    )
    collection.aggregate(aggregationPipeline).toFuture().map { result =>
      Ok(result.map(_.toJson()).mkString("[", ",", "]"))
    }
  }
  
}
