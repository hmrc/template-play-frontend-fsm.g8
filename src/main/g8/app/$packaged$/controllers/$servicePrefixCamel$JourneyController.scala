/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package $package$.controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.fsm.{JourneyController, JourneyIdSupport}
import $package$.connectors._
import $package$.journeys.$servicePrefixCamel$JourneyModel.State._
import $package$.models._
import $package$.services.$servicePrefixCamel$JourneyServiceWithHeaderCarrier
import $package$.wiring.AppConfig

import scala.concurrent.ExecutionContext

@Singleton
class $servicePrefixCamel$JourneyController @Inject() (
  appConfig: AppConfig,
  override val messagesApi: MessagesApi,
  val authConnector: FrontendAuthConnector,
  val env: Environment,
  controllerComponents: MessagesControllerComponents,
  views: $package$.views.Views,
  override val journeyService: $servicePrefixCamel$JourneyServiceWithHeaderCarrier,
  override val actionBuilder: DefaultActionBuilder
)(implicit val config: Configuration, ec: ExecutionContext)
    extends FrontendController(controllerComponents) with I18nSupport with AuthActions
    with JourneyController[HeaderCarrier] with JourneyIdSupport[HeaderCarrier] {

  final val controller = routes.$servicePrefixCamel$JourneyController

  import $servicePrefixCamel$JourneyController._
  import $package$.journeys.$servicePrefixCamel$JourneyModel._

  /** AsUser authorisation request */
  final val AsUser: WithAuthorised[Option[String]] = { implicit request =>
    authorisedWithEnrolment(appConfig.authorisedServiceName, appConfig.authorisedIdentifierKey)
  }

  /** Base authorized action builder */
  final val whenAuthorisedAsUser =
    actions.whenAuthorised(AsUser)

  /** Provide response when user have no required enrolment. */
  override def toSubscriptionJourney(continueUrl: String): Result = ???

  /** Dummy action to use only when developing to fill loose-ends. */
  final val actionNotYetImplemented = Action(NotImplemented)

  // Dummy URL to use when developing the journey
  final val workInProgresDeadEndCall =
    Call("GET", "/$serviceUrlPrefix$/work-in-progress")

  // YOUR ACTIONS

  final val showStart: Action[AnyContent] =
    actions.show[Start.type]

  /**
    * Function from the `State` to the `Call` (route),
    * used by play-fsm internally to create redirects.
    */
  final override def getCallFor(state: State)(implicit request: Request[_]): Call =
    state match {
      case Start => controller.showStart
      case _     => workInProgresDeadEndCall

    }

  import uk.gov.hmrc.play.fsm.OptionalFormOps._

  /**
    * Function from the `State` to the `Result`,
    * used by play-fsm internally to render the actual content.
    */
  final override def renderState(state: State, breadcrumbs: List[State], formWithErrors: Option[Form[_]])(implicit
    request: Request[_]
  ): Result =
    state match {
      case Start => Ok(views.startView())
      case _     => NotImplemented
    }

  // ------------------------------------
  // Retrieval of journeyId configuration
  // ------------------------------------

  private val journeyIdPathParamRegex = ".*?/journey/([A-Za-z0-9-]{36})/.*".r

  final override def journeyId(implicit rh: RequestHeader): Option[String] = {
    val journeyIdFromPath = rh.path match {
      case journeyIdPathParamRegex(id) => Some(id)
      case _                           => None
    }

    val idOpt = journeyIdFromPath
      .orElse(rh.session.get(journeyService.journeyKey))

    idOpt
  }

  final override implicit def context(implicit rh: RequestHeader): HeaderCarrier =
    appendJourneyId(super.hc)

  final override def amendContext(headerCarrier: HeaderCarrier)(key: String, value: String): HeaderCarrier =
    headerCarrier.withExtraHeaders(key -> value)
}

object $servicePrefixCamel$JourneyController {}
