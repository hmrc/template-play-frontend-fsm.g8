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
import $package$.journeys.$servicePrefix$JourneyModel.State._
import $package$.models._
import $package$.services.$servicePrefix$JourneyServiceWithHeaderCarrier
import $package$.wiring.AppConfig

import scala.concurrent.ExecutionContext

@Singleton
class $servicePrefix$JourneyController @Inject() (
  appConfig: AppConfig,
  authConnector: FrontendAuthConnector,
  environment: Environment,
  configuration: Configuration,
  controllerComponents: MessagesControllerComponents,
  views: $package$.views.Views,
  myJourneyService: $servicePrefix$JourneyServiceWithHeaderCarrier
) extends BaseJourneyController(
      myJourneyService,
      controllerComponents,
      appConfig,
      authConnector,
      environment,
      configuration
    ) {

  final val controller = routes.$servicePrefix$JourneyController

  import journeyService.model._
  import $servicePrefix$JourneyController._

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
}

object $servicePrefix$JourneyController {}
