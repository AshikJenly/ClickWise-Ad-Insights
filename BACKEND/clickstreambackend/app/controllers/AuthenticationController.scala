package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{Await,Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._
import play.api.libs.json._
import models._
import dao._

class AuthenticationController @Inject()(cc: ControllerComponents,userDAO: UsersDAO) extends AbstractController(cc){

         implicit val userFormat: Format[User] = Json.format[User]

    def index() = Action.async { implicit request: Request[AnyContent] =>
             userDAO.getUserByEmail("jenly@gmaul.com").map {
             case Some(user) => Ok("hai")
             case None => NotFound("User Not found")
         }
    }
        
    def register() = Action.async{implicit request: Request[AnyContent] =>
            val value = request.body.asJson.get
            val us = value.as[User]
            
            
            Future{
                userDAO.createUser(us)
                Ok("registered")
            }
    
    
}
}