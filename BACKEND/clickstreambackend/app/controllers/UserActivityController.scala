package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import dao._
import play.api.libs.json._
import scala.concurrent.{Await}
import scala.concurrent.duration.Duration
import models._
import services._
class UserActivityController @Inject()(cc: ControllerComponents,adDAO:AdDAO,produceService:ProduceDataToHubs) extends AbstractController(cc){

    implicit val user_activity: Format[UserActivity] = Json.format[UserActivity]

    def postactivity() = Action { implicit request:Request[AnyContent] =>
        val value = request.body.asJson.get
        val user_id = request.session.get("user_id").get
       
        val act = value.as[UserActivity]
        act.userId = user_id + ""
        println(act)
        produceService.produceUserActivity(value)
        Ok("Produced")
    }

} 