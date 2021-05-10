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
import $package$.views.CommonUtilsHelper.DateTimeUtilities

import scala.concurrent.ExecutionContext

@Singleton
class $servicePrefixCamel$JourneyController @Inject() (
  appConfig: AppConfig,
  override val messagesApi: MessagesApi,
  $servicePrefixcamel$ApiConnector: $servicePrefixCamel$ApiConnector,
  val authConnector: FrontendAuthConnector,
  val env: Environment,
  controllerComponents: MessagesControllerComponents,
  views: $package$.views.$servicePrefixCamel$Views,
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

  final val whenAuthorisedAsUserWithEori =
    actions.whenAuthorisedWithRetrievals(AsUser)

  /** Dummy action to use only when developing to fill loose-ends. */
  final val actionNotYetImplemented = Action(NotImplemented)

  // Dummy URL to use when developing the journey
  final val workInProgresDeadEndCall =
    Call("GET", "/$serviceUrlPrefixHyphen$/work-in-progress")

  // Redirection to the enrolment subscription journey
  final def toSubscriptionJourney(continueUrl: String): Result =
    Redirect(
      appConfig.subscriptionJourneyUrl,
      Map(
        "continue" -> Seq(continueUrl)
      )
    )

  // GET /
  final val showStart: Action[AnyContent] =
    whenAuthorisedAsUser
      .apply(Transitions.start)
      .display
      .andCleanBreadcrumbs()

  // GET /new/declaration-details
  final val showEnterDeclarationDetails: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.EnterDeclarationDetails]
      .orApply(Transitions.backToEnterDeclarationDetails)

  // POST /new/declaration-details
  final val submitDeclarationDetails: Action[AnyContent] =
    whenAuthorisedAsUserWithEori
      .bindForm(DeclarationDetailsForm)
      .apply(Transitions.submittedDeclarationDetails)

  // ----------------------- EXPORT QUESTIONS -----------------------

  // GET /new/export/request-type
  final val showAnswerExampleQuestionsRequestType: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.AnswerExampleQuestionsRequestType]
      .orApply(Transitions.backToAnswerExampleQuestionsRequestType)

  // POST /new/export/request-type
  final val submitExampleQuestionsRequestTypeAnswer: Action[AnyContent] =
    whenAuthorisedAsUserWithEori
      .bindForm(ExampleRequestTypeForm)
      .apply(Transitions.submittedExampleQuestionsAnswerRequestType)

  // GET /new/export/route-type
  final val showAnswerExampleQuestionsRouteType: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.AnswerExampleQuestionsRouteType]
      .orApply(Transitions.backToAnswerExampleQuestionsRouteType)

  // POST /new/export/route-type
  final val submitExampleQuestionsRouteTypeAnswer: Action[AnyContent] =
    whenAuthorisedAsUserWithEori
      .bindForm(ExampleRouteTypeForm)
      .apply(Transitions.submittedExampleQuestionsAnswerRouteType)

  // GET /new/export/summary
  final val showExampleQuestionsSummary: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.ExampleQuestionsSummary]

  // ----------------------- CONFIRMATION -----------------------

  // POST /new/create-case
  final def createCase: Action[AnyContent] =
    whenAuthorisedAsUserWithEori
      .applyWithRequest { implicit request =>
        Transitions.createCase($servicePrefixcamel$ApiConnector.createCase(_))
      }

  // GET /new/confirmation
  final def show$servicePrefixCamel$Confirmation: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.$servicePrefixCamel$Confirmation]
      .orRollback
      .andCleanBreadcrumbs() // forget journey history

  // GET /new/case-already-exists
  final def showCaseAlreadyExists: Action[AnyContent] =
    whenAuthorisedAsUser
      .show[State.CaseAlreadyExists]
      .orRollback

  /**
    * Function from the `State` to the `Call` (route),
    * used by play-fsm internally to create redirects.
    */
  final override def getCallFor(state: State)(implicit request: Request[_]): Call =
    state match {
      case Start =>
        controller.showStart

      case _: EnterDeclarationDetails =>
        controller.showEnterDeclarationDetails

      case _: AnswerExampleQuestionsRequestType =>
        controller.showAnswerExampleQuestionsRequestType

      case _: AnswerExampleQuestionsRouteType =>
        controller.showAnswerExampleQuestionsRouteType

      case _: ExampleQuestionsSummary =>
        controller.showExampleQuestionsSummary

      case _: $servicePrefixCamel$Confirmation =>
        controller.show$servicePrefixCamel$Confirmation

      case _: CaseAlreadyExists =>
        controller.showCaseAlreadyExists

      case _ =>
        workInProgresDeadEndCall

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

      case Start =>
        Redirect(controller.showEnterDeclarationDetails)

      case EnterDeclarationDetails(declarationDetailsOpt, _) =>
        Ok(
          views.declarationDetailsEntryView(
            formWithErrors.or(DeclarationDetailsForm, declarationDetailsOpt),
            controller.submitDeclarationDetails,
            controller.showStart
          )
        )

      case AnswerExampleQuestionsRequestType(model) =>
        Ok(
          views.exportQuestionsRequestTypeView(
            formWithErrors.or(ExampleRequestTypeForm, model.exportQuestionsAnswers.requestType),
            controller.submitExampleQuestionsRequestTypeAnswer,
            controller.showEnterDeclarationDetails
          )
        )

      case AnswerExampleQuestionsRouteType(model) =>
        Ok(
          views.exportQuestionsRouteTypeView(
            formWithErrors.or(ExampleRouteTypeForm, model.exportQuestionsAnswers.routeType),
            controller.submitExampleQuestionsRouteTypeAnswer,
            controller.showAnswerExampleQuestionsRequestType
          )
        )

      case ExampleQuestionsSummary(model) =>
        Ok(
          views.exportQuestionsSummaryView(
            model.declarationDetails,
            model.exportQuestionsAnswers,
            controller.createCase,
            controller.showAnswerExampleQuestionsRouteType
          )
        )

      case $servicePrefixCamel$Confirmation(_, _, $servicePrefixCamel$Result(caseReferenceId, generatedAt)) =>
        Ok(
          views.createCaseConfirmationView(
            caseReferenceId,
            generatedAt.ddMMYYYYAtTimeFormat,
            controller.showStart
          )
        )

      case CaseAlreadyExists(_) =>
        Ok(
          views.caseAlreadyExistsView(
            routes.$servicePrefixCamel$JourneyController.showStart
          )
        )

      case _ => NotImplemented

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

object $servicePrefixCamel$JourneyController {

  import FormFieldMappings._

  val DeclarationDetailsForm = Form[DeclarationDetails](
    mapping(
      "epu"         -> epuMapping,
      "entryNumber" -> entryNumberMapping,
      "entryDate"   -> entryDateMapping
    )(DeclarationDetails.apply)(DeclarationDetails.unapply)
  )

  val ExampleRequestTypeForm = Form[ExampleRequestType](
    mapping("requestType" -> exportRequestTypeMapping)(identity)(Option.apply)
  )

  val ExampleRouteTypeForm = Form[ExampleRouteType](
    mapping("routeType" -> exportRouteTypeMapping)(identity)(Option.apply)
  )
}
