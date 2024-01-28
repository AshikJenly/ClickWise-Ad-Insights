package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.Future
import views.html.adpages.{aboutpage, blogpage, productspage, servicespage}

class PageController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def checkLogin(request: Request[AnyContent]): Option[Boolean] = {
    request.session.get("islogin").map(_.toBoolean)
  }

  def blog() = Action{ implicit request: Request[AnyContent] =>
   
      checkLogin(request) match {
      case Some(true) => Ok(blogpage())
      case _ => Redirect("/auth/login")
    }
   
  }

  def about() = Action{ implicit request: Request[AnyContent] =>
    checkLogin(request) match {
      case Some(true) => Ok(aboutpage())
      case _ => Redirect("/auth/login")
    }
  }

  def products() = Action{ implicit request: Request[AnyContent] =>
    checkLogin(request) match {
      case Some(true) => Ok(productspage())
      case _ => Redirect("/auth/login")
    }
  }

  def services() = Action{ implicit request: Request[AnyContent] =>
    checkLogin(request) match {
      case Some(true) => Ok(servicespage())
      case _ => Redirect("/auth/login")
    }
  }
}
