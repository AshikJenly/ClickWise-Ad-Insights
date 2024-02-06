package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController,ControllerComponents,Request,AnyContent}


import views.html.authentication.{apidoc,loginpage,registerpage}


class AuthenticationPageController @Inject()(cc: ControllerComponents) extends AbstractController(cc){
 
 
 
    def index() = Action {implicit request: Request[AnyContent] =>
            Ok(apidoc())
        }
  
    def login() = Action {implicit request: Request[AnyContent] =>
            Ok(loginpage())
        }
    def register() = Action {implicit request: Request[AnyContent] =>
                Ok(registerpage())
            }


}