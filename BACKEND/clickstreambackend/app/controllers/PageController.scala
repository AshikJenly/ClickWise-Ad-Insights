package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import views.html.adpages.{aboutpage,blogpage,productspage,servicespage}

class PageController @Inject()(cc: ControllerComponents) extends AbstractController(cc){



    def blog() = Action.async{implicit request:Request[AnyContent] =>
    
            Ok(blogpage())
    }
     def about() = Action.async{implicit request:Request[AnyContent] =>
    
            Ok(aboutpage())
    }
     def products() = Action.async{implicit request:Request[AnyContent] =>
    
            Ok(productspage())
    }
     def services() = Action.async{implicit request:Request[AnyContent] =>
    
            Ok(servicespage())
    }

}
