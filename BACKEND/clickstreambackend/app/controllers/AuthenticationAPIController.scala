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

class AuthenticationAPIController @Inject()(cc: ControllerComponents,userDAO: UsersDAO) extends AbstractController(cc){

    case class Login(email:String,password:String)

    implicit val user_format: Format[User] = Json.format[User]
    implicit val login_format: Format[Login] = Json.format[Login]

   

    def index() = Action.async { implicit request: Request[AnyContent] =>
             userDAO.getUserByEmail("jenly@gmaul.com").map {
             case Some(user) => Ok("hai")
             case None => NotFound("User Not found")
         }
    }

    def login() = Action {implicit request:Request[AnyContent]=>
    
        val value = request.body.asJson.get
        val login_details:Login = value.as[Login]
        val exists_email = Await.result(userDAO.getUserByEmail(login_details.email),Duration.Inf)
        if(exists_email.isDefined) {
            val user = exists_email.get 
            val verify_password = Await.result(userDAO.getUserByPassAndEmail(email=login_details.email,password=login_details.password),Duration.Inf)
            if(verify_password.isDefined){
               
               Ok(Json.obj("status" -> "verified")).withSession("islogin" -> "true")
            //    Redirect("/")
            }
            else{
      Ok(Json.obj("status" -> "wrong password"))
            }
        }
        else {
    Ok(Json.obj("status" -> "Email Not Found"))
        }
       
    }    
   def logout() = Action {implicit request:Request[AnyContent]=>
        Ok(Json.obj("status" -> "verified")).withSession("islogin" -> "false")
   }
    def register() = Action.async{implicit request: Request[AnyContent] =>
            val value = request.body.asJson.get
            val user = value.as[User]     
            val exists_email = Await.result(userDAO.getUserByEmail(user.email),Duration.Inf)
            if(exists_email.isDefined) {
                Future{
                     Ok(Json.obj("status" -> "already exists"))
                }
              
            }
            else{
                  Future{
                    userDAO.createUser(user)
                     Ok(Json.obj("status" -> "verified"))
                }
            }
    
}
}