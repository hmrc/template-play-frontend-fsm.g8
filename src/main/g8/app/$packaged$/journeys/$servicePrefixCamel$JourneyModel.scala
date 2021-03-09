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

package $package$.journeys

import $package$.models._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import $package$.connectors.{ApiError, $servicePrefixCamel$CaseResponse, $servicePrefixCamel$Result, $servicePrefixCamel$$servicePrefixCamel$Request}

import uk.gov.hmrc.play.fsm.JourneyModel

object $servicePrefixCamel$JourneyModel extends JourneyModel {

  sealed trait State
  sealed trait IsError
  sealed trait IsTransient

  override val root: State = State.Start

  /** Model parametrization and rules. */
  object Rules {

    /** Checks is all export questions answers are in place. */
    final def isComplete(exportQuestionsStateModel: ExampleQuestionsStateModel): Boolean = {
      val answers = exportQuestionsStateModel.exportQuestionsAnswers

      answers.requestType.isDefined &&
      answers.routeType.isDefined
    }

  }

  /** All the possible states the journey can take. */
  object State {

    /** Root state of the journey. */
    case object Start extends State

    /** State intended to use only in the development of the model to fill loose ends. */
    case object WorkInProgressDeadEnd extends State

    // MARKER TRAITS

    sealed trait HasDeclarationDetails {
      def declarationDetails: DeclarationDetails
    }

    sealed trait HasQuestionsAnswers {
      def questionsAnswers: QuestionsAnswers
    }

    sealed trait HasExampleQuestionsStateModel {
      val model: ExampleQuestionsStateModel
    }

    // SPECIALIZED STATE TRAITS

    sealed trait ExampleQuestionsState
        extends State with HasDeclarationDetails with HasExampleQuestionsStateModel with HasQuestionsAnswers {
      final def declarationDetails: DeclarationDetails = model.declarationDetails
      final def exportQuestionsAnswers: ExampleQuestions = model.exportQuestionsAnswers
      final def questionsAnswers: QuestionsAnswers = exportQuestionsAnswers
    }

    sealed trait SummaryState extends State

    sealed trait EndState extends State

    // STATES

    final case class EnterDeclarationDetails(
      declarationDetailsOpt: Option[DeclarationDetails] = None,
      exportQuestionsAnswersOpt: Option[ExampleQuestions] = None
    ) extends State

    // EXPORT QUESTIONS STATES

    final case class AnswerExampleQuestionsRequestType(
      model: ExampleQuestionsStateModel
    ) extends ExampleQuestionsState

    final case class AnswerExampleQuestionsRouteType(
      model: ExampleQuestionsStateModel
    ) extends ExampleQuestionsState

    final case class ExampleQuestionsSummary(
      model: ExampleQuestionsStateModel
    ) extends ExampleQuestionsState with SummaryState

    // END-OF-JOURNEY STATES

    final case class $servicePrefixCamel$Confirmation(
      declarationDetails: DeclarationDetails,
      questionsAnswers: QuestionsAnswers,
      result: $servicePrefixCamel$Result
    ) extends EndState

    final case class CaseAlreadyExists(
      caseReferenceId: String
    ) extends EndState

  }

  /**
    * Function determining if all questions were answered
    * and the user can proceed straight to the summary,
    * or rather shall she go to the next question.
    */
  final def gotoSummaryIfCompleteOr(state: State): Future[State] =
    state match {
      case s: State.ExampleQuestionsState =>
        if (Rules.isComplete(s.model)) goto(State.ExampleQuestionsSummary(s.model))
        else goto(s)

      case s => goto(s)
    }

  /** This is where things happen a.k.a bussiness logic of the service. */
  object Transitions {
    import State._

    final val start =
      Transition {
        case _ => goto(Start)
      }

    final val backToEnterDeclarationDetails =
      Transition {
        case s: ExampleQuestionsState =>
          goto(
            EnterDeclarationDetails(
              Some(s.model.declarationDetails),
              exportQuestionsAnswersOpt = Some(s.model.exportQuestionsAnswers)
            )
          )

        case _ =>
          goto(EnterDeclarationDetails())
      }

    final def submittedDeclarationDetails(user: Option[String])(declarationDetails: DeclarationDetails) =
      Transition {
        case EnterDeclarationDetails(_, exportQuestionsOpt) =>
          gotoSummaryIfCompleteOr(
            AnswerExampleQuestionsRequestType(
              ExampleQuestionsStateModel(
                declarationDetails,
                exportQuestionsOpt.getOrElse(ExampleQuestions())
              )
            )
          )
      }

    final val backToAnswerExampleQuestionsRequestType =
      Transition {
        case s: ExampleQuestionsState if s.model.exportQuestionsAnswers.requestType.isDefined =>
          goto(AnswerExampleQuestionsRequestType(s.model))
      }

    final def submittedExampleQuestionsAnswerRequestType(user: Option[String])(exportRequestType: ExampleRequestType) =
      Transition {
        case AnswerExampleQuestionsRequestType(model) =>
          val updatedExampleQuestions = model.exportQuestionsAnswers.copy(requestType = Some(exportRequestType))
          gotoSummaryIfCompleteOr(AnswerExampleQuestionsRouteType(model.updated(updatedExampleQuestions)))
      }

    final val backToAnswerExampleQuestionsRouteType =
      Transition {
        case s: ExampleQuestionsState if s.model.exportQuestionsAnswers.routeType.isDefined =>
          goto(AnswerExampleQuestionsRouteType(s.model))
      }

    final def submittedExampleQuestionsAnswerRouteType(user: Option[String])(exportRouteType: ExampleRouteType) =
      Transition {
        case AnswerExampleQuestionsRouteType(model) =>
          gotoSummaryIfCompleteOr(
            ExampleQuestionsSummary(
              model.updated(model.exportQuestionsAnswers.copy(routeType = Some(exportRouteType)))
            )
          )
      }

    type $servicePrefixCamel$Api = $servicePrefixCamel$$servicePrefixCamel$Request => Future[$servicePrefixCamel$CaseResponse]

    final def createCase(createCaseApi: $servicePrefixCamel$Api)(eori: Option[String])(implicit ec: ExecutionContext) =
      Transition {
        case state: ExampleQuestionsSummary =>
          val request =
            $servicePrefixCamel$$servicePrefixCamel$Request(
              state.model.declarationDetails,
              state.model.exportQuestionsAnswers,
              eori
            )
          createCaseApi(request).flatMap { response =>
            if (response.result.isDefined)
              goto(
                $servicePrefixCamel$Confirmation(
                  state.model.declarationDetails,
                  state.model.exportQuestionsAnswers,
                  response.result.get
                )
              )
            else
              response.error match {
                case Some(ApiError("409", Some(caseReferenceId))) =>
                  goto(CaseAlreadyExists(caseReferenceId))

                case _ =>
                  val message = response.error.map(_.errorCode).map(_ + " ").getOrElse("") +
                    response.error.map(_.errorMessage).getOrElse("")
                  fail(new RuntimeException(message))
              }
          }
      }
  }
}
