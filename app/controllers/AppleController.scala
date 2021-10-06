package controllers

import play.api.mvc._

import javax.inject._

@Singleton
class AppleController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.apple())
  }
}
