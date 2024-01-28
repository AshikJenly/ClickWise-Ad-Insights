package controllers

import javax.inject._
import play.api._
import play.api.mvc._


class AdController @Inject()(cc: ControllerComponents,userDAO: UsersDAO) extends AbstractController(cc){

    def adData() = Action { implicit request:Request[AnyContent] =>

            Ok("received")
    }

} 