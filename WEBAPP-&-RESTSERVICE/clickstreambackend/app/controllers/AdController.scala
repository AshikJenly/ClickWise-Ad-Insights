package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import dao._
import play.api.libs.json._
import scala.concurrent.{Await}
import scala.concurrent.duration.Duration
import models._

class AdController @Inject()(cc: ControllerComponents,adDAO:AdDAO) extends AbstractController(cc){

    implicit val user_format: Format[AdModel] = Json.format[AdModel]

    def adData() = Action { implicit request:Request[AnyContent] =>
            val allads = Await.result(adDAO.getAllAds,Duration.Inf)
            
            Ok(Json.toJson(allads))
    }

} 